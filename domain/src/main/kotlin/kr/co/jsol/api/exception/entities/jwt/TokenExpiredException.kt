package kr.co.jsol.api.exception.entities.jwt

import kr.co.jsol.api.exception.BasicException

class TokenExpiredException : BasicException(401, "token_expired", "로그인 세션이 만료되었습니다.")
