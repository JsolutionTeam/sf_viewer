package kr.co.jsol.rda

import kr.co.jsol.domain.entity.rdacolumn.RdaColumnService
import kr.co.jsol.domain.entity.user.UserDetailServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/rda")
class RdaRest(
    private val rdaService: RdaService,
    private val rdaColumnService: RdaColumnService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/send/{siteSeq}")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    fun sendSiteSensorData(
        @PathVariable siteSeq: Long,
    ): Long {
        // host 정보 출력
        val loginUser = UserDetailServiceImpl.getAccountFromSecurityContext()
        log.info(
            """

                농촌지능원 서버로 농가번호가 ${siteSeq}인 농가의 미송신 센서 데이터 전송을 시작합니다.
                요청자 : ${loginUser.name}
                요청 시간 : ${System.currentTimeMillis()}
                요청 농가번호 : $siteSeq
            """.trimMargin()
        )

        val sendById = rdaService.sendById(siteSeq, rdaColumnService.list())
        log.info("농촌지능원 서버로 농가번호가 ${siteSeq}인 농가의 미송신 센서 데이터 전송을 완료했습니다.")
        log.info("sendById : $sendById")
        return sendById
    }

    @PostMapping("/send")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    fun sendAllSitesSensorData(): Long {
        // host 정보 출력
        val loginUser = UserDetailServiceImpl.getAccountFromSecurityContext()
        log.info(
            """

                농촌지능원 서버로 모든 농가의 미송신 센서 데이터 전송을 시작합니다.
                요청자 : ${loginUser.name}
                요청 시간 : ${System.currentTimeMillis()}
            """.trimMargin()
        )

        return rdaService.send(rdaColumnService.list())
    }
}
