package kr.co.jsol

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.integration.config.EnableIntegration

@SpringBootApplication(scanBasePackages = ["kr.co.jsol"])
@EnableIntegration // intergartion을 사용하려면 붙여야 한다., integration - tcp 사용 중.
class SocketApplication

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger(SocketApplication::class.java)
    log.info("SocketApplication start - v1.0.0")
    runApplication<SocketApplication>(*args)
}
