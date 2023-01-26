package kr.co.jsol.socket

import kr.co.jsol.domain.entity.ingsystem.InGSystemService
import kr.co.jsol.service.TcpInGSystemService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.Socket

@Component
class TcpRequestHandler(
    private val tcpInGSystemService: TcpInGSystemService,
) {
    // slf4j logger 생성
    private val log = LoggerFactory.getLogger(TcpRequestHandler::class.java)
    fun handle(socket: Socket){
        val hostAddress = socket.inetAddress.hostAddress
        val message = socket.getInputStream().bufferedReader().readLine()


        // socket의 requester 정보를 출력
        log.info("requester: $hostAddress")
        // socket의 inputStream을 읽어서 출력
        log.info("request: $message")


        tcpInGSystemService.handleTcpMessage(message, hostAddress,)
        // socket의 outputStream에 응답을 보낸다.
//        socket.getOutputStream().bufferedWriter().write("Hello World")
//        socket.getOutputStream().bufferedWriter().flush()
        // socket을 닫는다.
        socket.close()
    }
}
