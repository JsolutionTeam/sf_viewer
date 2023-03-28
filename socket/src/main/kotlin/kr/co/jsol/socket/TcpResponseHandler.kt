package kr.co.jsol.socket

import kr.co.jsol.domain.entity.site.GetSiteService
import kr.co.jsol.socket.interfaces.InGTcpHandler
import kr.co.jsol.socket.interfaces.TcpResponseHandlerInterface
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset

@Component
class TcpResponseHandler(
    private val siteService: GetSiteService,
) : TcpResponseHandlerInterface, InGTcpHandler {

    private val log = LoggerFactory.getLogger(TcpResponseHandler::class.java)
    fun handle(socket: Socket, outputStream: OutputStream, delay: Long) {
        // siteSeq로 site 정보 가져오기
        var message: String?

        // *System Time,Site delay# 순서로 데이터 보내기 ms값이 필요없다고 하여 1000으로 나눈 값을 보냄.
        val currentTimeMs = System.currentTimeMillis().div(1000)
        message = makeMessageFormat(currentTimeMs, delay)
        handle(outputStream, message)
    }

    override fun handle(outputStream: OutputStream, message: String) {
        try {
            log.info("보낼 데이터 : $message")
            var bytes: ByteArray? = message.toByteArray(Charset.forName("US-ASCII"))
            log.info("보낼 데이터 길이 : ${bytes?.size}")
            if (bytes == null) {
                log.info("bytes is null")
                return
            }

            // bytes = message.toByteArray(Charset.forName("UTF-8"))US-ASCII
            outputStream.write(bytes)
            outputStream.flush()
            log.info("[데이터 보내기 성공]")
        } catch (e: Exception) {
            log.error("[데이터 송신 중 에러발생] - ${e.message}")
            Thread.interrupted()
            throw e
        }
    }
}
