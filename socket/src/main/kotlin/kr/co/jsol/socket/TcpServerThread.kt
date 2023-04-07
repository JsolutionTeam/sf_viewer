package kr.co.jsol.socket

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketTimeoutException
import kotlin.system.exitProcess

@Configuration
class TcpServerThread(
    private val tcpRequestHandler: TcpRequestHandler,
    private val tcpResponseHandler: TcpResponseHandler,
) {
    // logger 생성
    private val log = LoggerFactory.getLogger(TcpServerThread::class.java)

    @Value("\${tcp.server.port:2332}")
    private val port: Int = 2332

    // socket 접속 정보
    private var socketDelay: HashMap<Socket, Long?> = hashMapOf()

    @Value("\${ingsystem.message.max-delay:600}")
    private val maxDelay: Int = 600

    @Value("\${ingsystem.message.send-delay:60}") // 서버 -> 센서 지연시간을 보내는 주기
    private var sendDelay: Int = 60

    // 잦은 요청은 broken pipe exception의 원인이다. 120초 이상으로 줘야 그나마 덜 발생한다.
    @Value("\${ingsystem.message.default-delay:120}") // 센서 기본 지연시간
    private val defaultSensorDelay: Long = 120

    @Bean
    fun tcpServer() = Thread {
        var serverSocket: ServerSocket? = null

        // 기기가 보내는 delay가 최대 10분인데 10분보단 큰 값으로 타임아웃을 걸어야 함
        // 인지시스템 이상은 대표님께서는 최대 지연시간 * 2 + 10으로 설정하는것이 좋다고 함.
        val socketTimeout = (maxDelay * 1000 + 10) * 1000
        log.info(
            """

            |=======================================
            |[TCP 서버 시작]
            | - 기본 정보 출력
            |   - 최대 지연시간 : ${(maxDelay * 2 + 10)} sec
            |   - socketTimeout = $socketTimeout
            |=======================================
            """.trimIndent()
        )
        try { // server socket try

            serverSocket = ServerSocket(port)
            serverSocket.soTimeout = Int.MAX_VALUE - 10006 // accept 할 때 X ms간 인식이 없으면 SocketTimeoutException 발생
            var socket: Socket?
            while (true) {
                // 연결 수락
                try {
                    socket = serverSocket.accept() // 클라이언트가 접속해 오기를 기다리고, 접속이 되면 통신용 socket 을 리턴한다.
                } catch (e: SocketTimeoutException) {
                    continue
                }
                log.info(
                    """

                    [현재 접속 정보] ${socket.inetAddress.hostAddress}, ${socket.port}
                    [현재 실행중인 스레드 수] ${Thread.activeCount()}
                    """.trimIndent()
                )

                socket.soTimeout = socketTimeout
                socket.keepAlive = true
                val hostAddress = socket.inetAddress.hostAddress
                log.info("[연결 수락함] $hostAddress, ${socket.port}")
                // 연결 성공 시 초기화 용 데이터 전송 현재 시간, 지연 시간
                val siteDelay = tcpRequestHandler.getSiteDelayByIp(hostAddress)
                log.info("[첫 데이터 전송] - 지연 시간 : $siteDelay")
                tcpResponseHandler.handle(socket, socket.getOutputStream(), siteDelay)

                log.info("[첫 데이터 전송 완료] - Thread 생성 및 시작")

//                val t = Thread {
//                    try {
// //                        process(socket)
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    } finally {
//                    }
//                }
//                t.start()

                val inProcessThread = InProcessThread(socket)
                Thread(inProcessThread).start()
                val outProcessThread = OutProcessThread(socket)
                Thread(outProcessThread).start()

                // 현재 실행중인 스레드 수 표시
                log.info("[현재 실행중인 스레드 수] ${Thread.activeCount()}")
            }
        } catch (e: IOException) { // server socket catch
            e.printStackTrace()
            log.error("[소켓 서버 처리 중 에러 발생] - ${e.message}")
        } finally { // server socket finally
            log.info(
                """

                =========  ==     ==  ======
                ==         ====   ==  ==    ==
                =========  == ==  ==  ==    ==
                ==         ==  == ==  ==    ==
                =========  ==   ====  ======

                [TCP 서버] 종료
                """.trimIndent()
            )
            serverSocket?.close()
            exitProcess(0)
        }
    }

    inner class InProcessThread(socket: Socket) : Runnable {
        private val socket: Socket = socket
        override fun run() {
            process(this.socket)
        }
    }

    fun process(socket: Socket) {
        log.info(
            """

                |=======================================
                |[데이터 처리 쓰레드 시작]
                | - SOCKET 정보 출력
                | - ${socket.inetAddress.hostAddress}, ${socket.port}
                |=======================================
            """.trimIndent()
        )
        var message: String?
        var inputStream: InputStream = socket.getInputStream()
        try {
            while (true) {
                if (socket.isClosed) break

                // 읽을 데이터가 있을 때만 진행
                if (inputStream.available() > 0) {
                    log.info(
                        """

                    |=======================================
                    |[데이터 입력 작업 시작]
                    |=======================================
                        """.trimIndent()
                    )

                    val buffer = BufferedReader(InputStreamReader(inputStream))
                    log.info("[데이터 버퍼 적재 완료] buffer is ready: ${buffer.ready()}")

                    // 버퍼가 준비 되었을 때만 처리
                    if (buffer.ready()) {
                        message = buffer.readLine()
                        log.info("[데이터 받기 성공] - $message")
                        if (message.isNullOrEmpty()) {
                            log.info("[데이터 처리 실패] - $message")
                            throw Exception("데이터 처리 실패")
                        }
                        // 데이터 처리
                        val delay = tcpRequestHandler.handle(socket!!, message!!)
                        socketDelay[socket] = delay
                    }
                }

                // 소켓 통신 속도 딜레이를 줘 너무 잦은 프로세싱은 하지 않도록 함
                Thread.sleep(1000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            log.error("[TCP 서버] 에러 발생, 스트림 소켓 종료 후 로깅, 메세지 : $e")
        } finally {
            log.info(
                """

                            |=======================================
                            |[데이터 처리 종료]
                            | - SOCKET 정보 출력
                            | - ${socket?.inetAddress?.hostAddress}, ${socket?.port}
                            | - socket is closed: ${socket!!.isClosed}
                            |=======================================
                """.trimIndent()
            )

            try {
                socket?.let {
                    if (it is Closeable && !it.isClosed) {
                        socket?.inputStream?.let { inputStream ->
                            if (inputStream != null && inputStream is Closeable) {
                                inputStream.close()
                            }
                        }
                        socket?.outputStream.let { outputStream ->
                            if (outputStream is Flushable) {
                                outputStream.flush()
                            }
                            if (outputStream != null && outputStream is Closeable) {
                                outputStream.close()
                            }
                        }
                        it.close()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inner class OutProcessThread(socket: Socket) : Runnable {
        private val socket: Socket = socket
        private lateinit var outputStream: OutputStream

        init {
            log.info("Out Process Thread 생성")
        }

        override fun run() {

            try {
                while (true) {
                    // 계속 데이터를 보내기 위해선 반복해야 한다.
                    log.info(
                        """

                        ============ out process thread ==============
                        Thread Id : ${Thread.currentThread().id}
                        socket Info : ${socket.inetAddress.hostAddress}, ${socket.port}
                        ==========================================
                        """.trimIndent()
                    )
                    // delay 단위로 데이터 전송

                    if (socketDelay[socket] == null) {
                        Thread.sleep(5 * 1000) // socketDelay가 null이면 n 초간 대기를 반복해서 delay를 가져오는 것을 기다린다.
                    }

                    this.outputStream = socket.getOutputStream()
                    log.info("socketDelay[socket] : ${socketDelay[socket]}")
                    val delay: Long = socketDelay[socket] ?: defaultSensorDelay
                    tcpResponseHandler.handle(socket, outputStream, delay)

                    // sendDelay(15초)마다 각 기기에 딜레이 시간 변경하도록 수정
                    log.info("[데이터 전송 완료] - #${socket.inetAddress.hostAddress}:${socket.port}# ${sendDelay}만큼 지연됩니다.")
                    Thread.sleep((sendDelay * 1000).toLong())
                    log.info("[지연 종료]")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                log.error("[데이터 전송 에러] ${e.message}")
            } finally {
                log.info("[데이터 전송 종료] - ")
                if (outputStream is Flushable) outputStream?.flush()
                if (outputStream is Closeable) outputStream?.close()
            }
        }
    }
}
