package kr.co.jsol.api.exception.entities.jwt

import kr.co.jsol.api.exception.BasicException

class TokenIsNotValidException : BasicException(400, "token_not_valid", "토큰정보가 비정상입니다.")
