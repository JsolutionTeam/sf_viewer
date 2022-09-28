package kr.co.jsol.api.exception

class BadRequestException : BasicException {
    constructor() : super(400, "bad_request", "잘못된 요청입니다. 데이터를 확인해주세요.") {}
    constructor(message: String) : super(400, "bad_request", message) {}
    constructor(entityName: String, message: String) : super(400, entityName + "_bad_request", message) {}
}
