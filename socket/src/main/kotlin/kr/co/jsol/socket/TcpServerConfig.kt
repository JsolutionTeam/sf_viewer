package kr.co.jsol.socket

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.ServerSocket

@Configuration
class TcpServerConfig(
    private val tcpRequestHandler: TcpRequestHandler,
) {
    // logger 생성
    private val log = LoggerFactory.getLogger(TcpServerConfig::class.java)

    @Bean
    fun tcpServer() = Thread {
        val serverSocket = ServerSocket(2332)

        while (true) {
            try {
                val socket = serverSocket.accept()
                tcpRequestHandler.handle(socket)
            } catch (e: Exception) {
                log.error("tcpServer error", e.message)
            }
        }
    }
}
