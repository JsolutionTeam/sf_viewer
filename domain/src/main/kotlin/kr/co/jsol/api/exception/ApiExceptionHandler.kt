package kr.co.jsol.api.exception

import kr.co.jsol.api.exception.storage.StorageException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ApiExceptionHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ApiErrorResponse> {
        ex.printStackTrace()
        val response: ApiErrorResponse = ApiErrorResponse.create()
            .status(400)
            .code("error-request-body-missing-400")
            .message("request body json을 입력해주세요")
        return ResponseEntity<ApiErrorResponse>(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(StorageException::class)
    fun storageException(ex: StorageException): ResponseEntity<ApiErrorResponse> {
        val response: ApiErrorResponse = ApiErrorResponse.create()
            .status(500)
            .code("error-storage-500")
            .message(ex.message)
        return ResponseEntity<ApiErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    // @Valid 검증 실패 시 Catch
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ApiErrorResponse> {
        val errorMessage = e.bindingResult
            .allErrors[0]
            .defaultMessage
        val response: ApiErrorResponse = ApiErrorResponse.create()
            .status(400)
            .code(errorMessage)
            .message(e.toString())
        return ResponseEntity<ApiErrorResponse>(response, HttpStatus.BAD_REQUEST)
    }

    // @Valid 검증 실패 시 Catch
    @ExceptionHandler(InvalidParameterException::class)
    protected fun handleInvalidParameterException(e: InvalidParameterException): ResponseEntity<ApiErrorResponse> {
        logger.error("handleInvalidParameterException", e)
        val response: ApiErrorResponse = ApiErrorResponse.create()
            .status(400)
            .code(e.code)
            .message(e.message)
            .errors(e.errors)
        return ResponseEntity<ApiErrorResponse>(
            response,
            HttpStatus.resolve(response.status)
                ?: HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun dataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<ApiErrorResponse> {
        logger.error(ex.message)
        val response: ApiErrorResponse = ApiErrorResponse.create()
            .status(400)
            .code("error-value-valid")
            .message(
                """
    데이터가 제약조건 위반됩니다.
    ${ex.message}
                """.trimIndent()
            )
        return ResponseEntity<ApiErrorResponse>(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BasicException::class)
    fun unauthorizedException(ex: BasicException): ResponseEntity<ApiErrorResponse> {
        ex.printStackTrace()
        val response: ApiErrorResponse = ApiErrorResponse.create()
            .status(ex.status)
            .code(ex.code)
            .message(ex.message)

        // null이면 500
        return ResponseEntity<ApiErrorResponse>(
            response,
            HttpStatus.resolve(ex.status) ?: HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    protected fun illegalArgumentExceptionhandleException
    (e: IllegalArgumentException?): ResponseEntity<ApiErrorResponse> {
        logger.error("handleException...", e)
        val response: ApiErrorResponse = ApiErrorResponse.create()
            .status(500)
            .code(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
            .message("서버 오류")
        return ResponseEntity<ApiErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    // 모든 예외를 핸들링하여 ErrorResponse 형식으로 반환한다.
    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<ApiErrorResponse> {
        logger.error("handleException...", e)
        val response: ApiErrorResponse = ApiErrorResponse.create()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value().toString().toInt())
            .code(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
            .message(e.toString())
        return ResponseEntity<ApiErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
