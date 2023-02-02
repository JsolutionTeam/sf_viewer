package kr.co.jsol.common.exception

class UnauthorizedException(message: String = "계정 정보 인증에 실패했습니다.") : BasicException(401, message)
