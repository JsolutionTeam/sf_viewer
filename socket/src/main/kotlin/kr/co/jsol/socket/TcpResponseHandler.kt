package kr.co.jsol.socket

import kr.co.jsol.domain.entity.site.SiteService
import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.OutputStreamWriter
import java.io.Writer
import java.net.Socket
import java.util.*


@Component
class TcpResponseHandler(
    private val siteService: SiteService,
) {
    // slf4j logger 생성
    private val log = LoggerFactory.getLogger(TcpResponseHandler::class.java)
    fun handle(socket: Socket, siteSeq: Long) {
        // siteSeq로 site 정보 가져오기
        val site: SiteResponse = siteService.getById(siteSeq)

        val out: Writer = OutputStreamWriter(socket.getOutputStream())
        val now = Date()
        out.write(now.toString() + "\r\n")
        out.flush()

//        // 반환을 위한 객체
//        val outputStream = socket.getOutputStream()
//        val out: Writer = OutputStreamWriter(outputStream)
//        val responseStr: String = ""
//
//        // 현재 시간을 보냄
//        val serverTime = System.currentTimeMillis().toString() + "\r\n"
//        responseStr.plus(serverTime)
//        log.info("serverTime: $serverTime")
//
//        // delay 설정 시간 보냄
//        val delay = site.delay.toString() + "\r\n"
//        responseStr.plus(delay)
//        log.info("delay: $delay")
//
//        // 데이터 반환
//        out.write(responseStr)
//
//        // 데이터 send
//        out.flush()
    }
}
