package kr.co.jsol.common.exception.entities.user

import kr.co.jsol.common.exception.BasicException

class UserDisableException : BasicException(400, "사용 불가능한 계정 정보입니다.")
