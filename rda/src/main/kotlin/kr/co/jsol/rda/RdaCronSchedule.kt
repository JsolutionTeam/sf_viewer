package kr.co.jsol.rda

import kr.co.jsol.domain.entity.rdacolumn.RdaColumnService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RdaCronSchedule(
    private val rdaService: RdaService,
    private val rdaColumnService: RdaColumnService,
) {

    @Scheduled(cron = "0 30 18 * * *") // 매일 18시 30분에 실행
    fun rdaSend() {
        rdaService.send(rdaColumnService.list())
    }
}
