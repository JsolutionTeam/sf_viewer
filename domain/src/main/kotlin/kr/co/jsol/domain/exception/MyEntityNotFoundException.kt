package kr.co.jsol.domain.exception

class MyEntityNotFoundException : BasicException {
    constructor() : super(400, "not_found", "데이터를 찾을 수 없습니다.") {}
    constructor(entityName: String, message: String = "") : super(400, entityName + "_not_found", message) {}
}
