package kr.co.jsol.domain.exception

class InternalServerException @JvmOverloads constructor(message: String = "서버 에러 발생") : BasicException(500, message) {
}
