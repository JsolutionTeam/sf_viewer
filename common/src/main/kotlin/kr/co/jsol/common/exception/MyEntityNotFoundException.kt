package kr.co.jsol.common.exception

class MyEntityNotFoundException : BasicException {
    constructor() : super(404, "데이터를 찾을 수 없습니다.") {}
    constructor(message: String = "") : super(404, message) {}
}
