package kr.co.jsol.domain.exception

class MyEntityNotFoundException : BasicException {
    constructor() : super(400, "데이터를 찾을 수 없습니다.") {}
    constructor(message: String = "") : super(400, message) {}
}
