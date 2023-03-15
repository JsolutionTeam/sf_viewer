package kr.co.jsol.socket

import kr.co.jsol.domain.entity.site.SiteService
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class TcpServerRunner(
    private val tcpServer: Thread,
    private val siteService: SiteService,
) : ApplicationRunner {

    private val log = LoggerFactory.getLogger(TcpServerRunner::class.java)
    init {
        println("TcpServerRunner init")
        // 데이터베이스 첫 커넥션이 오래걸리기 때문에 미리 site 조회를 한 번 해본다.
        siteService.getSiteList().forEach {
            log.info("site: $it")
        }
    }

    override fun run(args: ApplicationArguments) {
        tcpServer.start()
    }
}
