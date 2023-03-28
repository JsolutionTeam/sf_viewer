package kr.co.jsol

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kr.co.jsol"])
class SocketApplication

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger(SocketApplication::class.java)
    log.info("SocketApplication start - v1.1.0")
    runApplication<SocketApplication>(*args)
}
