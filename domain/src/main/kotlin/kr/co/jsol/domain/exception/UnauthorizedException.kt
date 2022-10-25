package kr.co.jsol.domain.exception

class UnauthorizedException : BasicException {
    constructor() : super(401, "unauthorized", "계정 정보 인증에 실패했습니다.") {}
    constructor(message: String = "") : super(401, "unauthorized", message) {}
}
