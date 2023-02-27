package kr.co.jsol.socket

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.net.ServerSocket
import java.net.Socket
import java.util.*


@Configuration
class TcpServerConfig(
    private val tcpRequestHandler: TcpRequestHandler,
    private val tcpResponseHandler: TcpResponseHandler,
) {
    // logger 생성
    private val log = LoggerFactory.getLogger(TcpServerConfig::class.java)

    @Bean
    fun tcpServer() = Thread {
        val serverSocket = ServerSocket(2332)
        while(true){
            val socket = serverSocket.accept()
            log.info("socket accepted, $socket")
            val message = socket.getInputStream().bufferedReader().readLine()
            log.info("request: $message")

            val out: Writer = OutputStreamWriter(socket.getOutputStream())
            val now = Date()
            out.write(now.toString() + "\r\n")  // 서버에서 데이터에 개행 문자를 추가하여 반환
            out.flush()
        }



//        var serverSocket: ServerSocket? = null
//
//        try{
//            serverSocket = ServerSocket(2332)
//            while (serverSocket.isBound && !serverSocket.isClosed) {
//                var socket: Socket? =null
//                try {
//                    socket = serverSocket.accept()
//                    log.info("socket accepted, $socket")
//                    // 데이터 받는 부분
//                    val siteSeq = tcpRequestHandler.handle(socket)
//
//                    // 데이터 보내는 부분
//                    // 현재 서버시간(이 시간을 기준으로 reset), => 감우 처리를 위해
//                    // delay 걸을 시간
//                    val out: Writer = OutputStreamWriter(socket.getOutputStream())
//                    val now = Date()
//                    out.write(now.toString() + "\r\n")
//                    out.flush()
////                tcpResponseHandler.handle(socket, siteSeq)
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    // 서버 커넥션 하는 도중 커넥션이 끊어짐
//                    log.error("Occurred error - ${e.message}")
//                }finally {
//                    // socket 닫기
//                    socket?.close()
//                }
//            }
//        }catch (e: IOException){
//            e.printStackTrace()
//        }finally {
//            serverSocket?.close()
//        }
    }
}
