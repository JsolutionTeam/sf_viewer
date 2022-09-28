package kr.co.jsol.api.exception

class ExternalApiRequestException(message: String = "", code: String = "") : BasicException(500, code, message)
