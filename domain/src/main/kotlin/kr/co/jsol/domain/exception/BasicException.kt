package kr.co.jsol.domain.exception

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "에러")
@JsonInclude(JsonInclude.Include.NON_NULL)
open class BasicException(

    @field:Schema(description = "상태")
    val status: Int = 400,

    @field:Schema(description = "메세지")
    override var message: String = "",

) : RuntimeException() {

    // 무조건 안나오게 설정
    @field:Schema(hidden = true)
    override var cause: Throwable? = null

    // 무조건 안나오게 설정
    @field:Schema(hidden = true)
    val stackTrace: List<StackTraceElement>? = null
}
