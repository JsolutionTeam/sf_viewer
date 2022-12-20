package kr.co.jsol.domain.exception

class ForbiddenException @JvmOverloads constructor(
    message: String = "권한이 없습니다."
) : BasicException(403, message)
