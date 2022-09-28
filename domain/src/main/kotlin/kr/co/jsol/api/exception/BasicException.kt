package kr.co.jsol.api.exception

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "에러")
open class BasicException(
    @Schema(description = "상태")
    val status: Int = 400,

    @Schema(description = "코드")
    val code: String = "",

    @Schema(description = "메세지")
    override var message: String = "",
) : RuntimeException()
