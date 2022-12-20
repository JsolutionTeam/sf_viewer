package kr.co.jsol.domain.exception.entities.user

import kr.co.jsol.domain.exception.BasicException

class UserAlreadyExistUserException : BasicException(409, "이미 회원가입된 아이디입니다.")
