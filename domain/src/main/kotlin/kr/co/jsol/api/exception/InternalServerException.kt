package kr.co.jsol.api.exception

class InternalServerException : BasicException {
    @JvmOverloads
    constructor(message: String = "서버 에러 발생") : super(500, "internal server error", message) {
    }

    constructor(entityName: String, message: String = "") :
        super(500, entityName + "_internal server error", message)
}
