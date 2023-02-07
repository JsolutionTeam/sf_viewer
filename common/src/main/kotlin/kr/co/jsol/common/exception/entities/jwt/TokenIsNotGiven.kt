package kr.co.jsol.common.exception.entities.jwt

import kr.co.jsol.common.exception.BasicException

class TokenIsNotGiven(name: String) : BasicException(400, name + "토큰이 넘어오지 않았습니다.")
