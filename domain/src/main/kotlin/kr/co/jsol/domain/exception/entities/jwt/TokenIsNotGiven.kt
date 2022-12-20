package kr.co.jsol.domain.exception.entities.jwt

import kr.co.jsol.domain.exception.BasicException

class TokenIsNotGiven(name: String) : BasicException(400, name + "토큰이 넘어오지 않았습니다.")
