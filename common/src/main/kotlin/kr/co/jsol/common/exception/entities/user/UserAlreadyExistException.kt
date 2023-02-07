package kr.co.jsol.common.exception.entities.user

import kr.co.jsol.common.exception.BasicException

class UserAlreadyExistException : BasicException(409, "이미 회원가입된 아이디입니다.")
