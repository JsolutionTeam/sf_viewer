package kr.co.jsol.api.exception

import org.springframework.validation.Errors

class InvalidParameterException(val errors: Errors) : BasicException(400, "bad_request_parameters", "불가능한 요청 데이터입니다.") {
    companion object {
        private const val serialVersionUID = -2116671122895194101L
    }
}
