package kr.co.jsol.domain.exception.entities.jwt

import kr.co.jsol.domain.exception.BasicException

class TokenExpiredException : BasicException(401, "로그인 세션이 만료되었습니다.")
