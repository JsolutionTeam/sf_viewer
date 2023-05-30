package kr.co.jsol.rda

import kr.co.jsol.domain.entity.user.UserDetailServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/rda")
class RdaRest(
    private val rdaService: RdaService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/send/{siteSeq}")
    @ResponseStatus(value = HttpStatus.OK)
    fun sendSiteSensorData(
        @PathVariable siteSeq: Long,
    ): String {
        // host 정보 출력
        val loginUser = UserDetailServiceImpl.getAccountFromSecurityContext()
        log.info(
            """

                농촌지능원 서버로 농가번호가 ${siteSeq}인 농가의 미송신 센서 데이터 전송을 시작합니다.
                요청자 : ${loginUser.name}
                요청 시간 : ${System.currentTimeMillis()}
            """.trimMargin()
        )
        rdaService.sendById(siteSeq)
        return "OK"
    }

    @PostMapping("/send")
    @ResponseStatus(value = HttpStatus.OK)
    fun sendAllSitesSensorData(): String {
        // host 정보 출력
        val loginUser = UserDetailServiceImpl.getAccountFromSecurityContext()
        log.info(
            """

                농촌지능원 서버로 모든 농가의 미송신 센서 데이터 전송을 시작합니다.
                요청자 : ${loginUser.name}
                요청 시간 : ${System.currentTimeMillis()}
            """.trimMargin()
        )
        rdaService.send()
        return "OK"
    }
}
