package kr.co.jsol.socket.service

import org.springframework.stereotype.Service

//object-to-string-transformer transformer annotation이 있긴 함.
class EchoService {
    fun test(input: String): String {
        if ("FAIL" == input) {
            throw RuntimeException("Failure Demonstration")
        }
        return "echo:$input"
    }
}
