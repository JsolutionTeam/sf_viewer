package kr.co.jsol.socket

import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.socket.service.TcpSensorService
import kr.co.jsol.socket.interfaces.InGTcpHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.Socket

@Component
class TcpRequestHandler(
    private val tcpSensorService: TcpSensorService,
) : InGTcpHandler {
    // slf4j logger 생성
    private val log = LoggerFactory.getLogger(TcpRequestHandler::class.java)
    fun handle(socket: Socket, message: String): Site {
        val hostAddress = socket.inetAddress.hostAddress
        return tcpSensorService.handleTcpMessage(message, hostAddress)
    }

    fun findSiteByIp(ip: String): Site? {
        return tcpSensorService.findSiteByIp(ip)
    }

    fun getSiteDelayByIp(ip: String): Long {
        return tcpSensorService.getSiteDelayByIp(ip)
    }
}
