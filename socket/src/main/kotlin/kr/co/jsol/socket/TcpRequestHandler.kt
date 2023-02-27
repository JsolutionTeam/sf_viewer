package kr.co.jsol.socket

import kr.co.jsol.service.TcpSensorService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.Socket

@Component
class TcpRequestHandler(
    private val tcpSensorService: TcpSensorService,
) {
    // slf4j logger 생성
    private val log = LoggerFactory.getLogger(TcpRequestHandler::class.java)
    fun handle(socket: Socket): Long {
        val hostAddress = socket.inetAddress.hostAddress
        // readLine = 라인 단위로 데이터를 읽어들이기 때문에, 클라이언트 측에서 라인을 종료하지 않으면 readLine() 함수는 계속해서 데이터를 읽어들이지 않습니다.
//        val message = socket.getInputStream().bufferedReader().readLine()
        val message = socket.getInputStream().bufferedReader().readText()

        // socket의 requester 정보를 출력
        log.info("requester: $hostAddress")
        // socket의 inputStream을 읽어서 출력
        log.info("request: $message")

        return tcpSensorService.handleTcpMessage(message, hostAddress,)
    }
}
