package kr.co.jsol.api.exception.entities.jwt

import kr.co.jsol.api.exception.BasicException

class TokenIsNotGiven(name: String) : BasicException(400, "token_is_not_null", name + "토큰이 넘어오지 않았습니다.")
