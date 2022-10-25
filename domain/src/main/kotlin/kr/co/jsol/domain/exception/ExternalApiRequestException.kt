package kr.co.jsol.domain.exception

class ExternalApiRequestException(message: String = "", code: String = "") : BasicException(500, code, message)
