package kr.co.jsol.domain.exception

class BadRequestException(message: String = "잘못된 요청입니다. 데이터를 확인해주세요.") : BasicException(400, message) {
}
