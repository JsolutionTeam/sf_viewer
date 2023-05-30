package kr.co.jsol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["kr.co.jsol"])
class RdaApplication

fun main(args: Array<String>) {
    runApplication<RdaApplication>(*args)
}
