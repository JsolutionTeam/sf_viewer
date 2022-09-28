package kr.co.jsol.api.exception.entities.user

import kr.co.jsol.api.exception.BasicException

class UserDisableException : BasicException(400, "bad request", "사용 불가능한 계정 정보입니다.")
