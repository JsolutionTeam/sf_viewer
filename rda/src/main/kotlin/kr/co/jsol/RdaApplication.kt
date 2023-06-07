package kr.co.jsol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling // cron job Scheduled 사용을 위해 필요
@SpringBootApplication(scanBasePackages = ["kr.co.jsol"])
class RdaApplication

fun main(args: Array<String>) {
    runApplication<RdaApplication>(*args)
}
