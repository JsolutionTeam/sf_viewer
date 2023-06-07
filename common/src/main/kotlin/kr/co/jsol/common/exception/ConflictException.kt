package kr.co.jsol.common.exception

import org.springframework.http.HttpStatus

class ConflictException @JvmOverloads constructor(
    message: String = "중복된 데이터가 존재합니다."
) : BasicException(HttpStatus.CONFLICT.value(), message)
