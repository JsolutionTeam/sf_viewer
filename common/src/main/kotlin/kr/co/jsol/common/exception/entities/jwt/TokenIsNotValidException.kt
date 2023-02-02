package kr.co.jsol.common.exception.entities.jwt

import kr.co.jsol.common.exception.BasicException

class TokenIsNotValidException : BasicException(400, "토큰정보가 비정상입니다.")
