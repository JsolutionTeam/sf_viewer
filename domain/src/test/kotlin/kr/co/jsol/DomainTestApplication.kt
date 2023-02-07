package kr.co.jsol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// 도메인 모듈에서 테스트를 작성하기 위해 추가해준 클래스 파일.
@SpringBootApplication
class DomainTestApplication

fun main(args: Array<String>) {
    runApplication<DomainTestApplication>(*args)
}
