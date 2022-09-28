package kr.co.jsol.api.exception

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import java.time.LocalDateTime

class ApiErrorResponse {
    val timestamp = LocalDateTime.now()
    var code: String? = "error-server" // 예외를 세분화하기 위한 사용자 지정 코드,
    var message: String? = "처리되지 않은 예외입니다."
    var status: Int = 500 // HTTP 상태 값 저장 400, 404, 500 등..

    // 예외처리 메시지
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("errors")
    private var customFieldErrors: MutableList<CustomFieldError>? = null
    fun code(code: String?): ApiErrorResponse {
        this.code = code
        return this
    }

    fun status(status: Int): ApiErrorResponse {
        this.status = status
        return this
    }

    fun message(message: String?): ApiErrorResponse {
        this.message = message
        return this
    }

    fun errors(errors: Errors?): ApiErrorResponse {
        setCustomFieldErrors(errors!!.fieldErrors)
        return this
    }

    // BindingResult.getFieldErrors() 메소드를 통해 전달받은 fieldErrors
    private fun setCustomFieldErrors(fieldErrors: List<FieldError?>) {
        customFieldErrors = ArrayList()
        fieldErrors.forEach {
            if (it != null) {
                var code = ""
                if (it.codes != null && it.codes!!.isNotEmpty()) code = it.codes!![0]
                (customFieldErrors as ArrayList<CustomFieldError>).add(
                    CustomFieldError(
                        code,
                        it.rejectedValue,
                        it.defaultMessage
                    )
                )
            }
        }
    }

    // parameter 검증에 통과하지 못한 필드가 담긴 클래스이다.
    class CustomFieldError(val field: String?, val value: Any?, val reason: String?)
    companion object {
        fun create(): ApiErrorResponse {
            return ApiErrorResponse()
        }
    }
}
