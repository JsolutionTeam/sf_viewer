package kr.co.jsol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.integration.config.EnableIntegration

@SpringBootApplication(scanBasePackages = ["kr.co.jsol"])
@EnableIntegration // intergartion을 사용하려면 붙여야 한다., integration - tcp 사용 중.
class SocketApplication

fun main(args: Array<String>) {
    runApplication<SocketApplication>(*args)
}
