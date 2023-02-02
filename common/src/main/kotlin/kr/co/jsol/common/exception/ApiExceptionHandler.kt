package kr.co.jsol.common.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import kr.co.jsol.common.exception.storage.StorageException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class ApiExceptionHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(StorageException::class)
    fun storageException(ex: StorageException): ResponseEntity<BadRequestException> {
        val message = ex.message
        logger.error("storageException - message : $message")
        return ResponseEntity<BadRequestException>(
            BadRequestException(message ?: "파일 저장 중 오류가 발생했습니다."),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    // MethodArgumentNotValidException - @Valid 검증 실패 시 Catch된다.
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<BadRequestException> {
        val message = e.bindingResult
            .allErrors[0]
            .defaultMessage ?: REQUEST_BODY_ERROR
        logger.error("handleMethodArgumentNotValidException - message : $message")
        return ResponseEntity<BadRequestException>(
            BadRequestException(
                message
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    // @Valid 검증 실패 시 Catch
    @ExceptionHandler(InvalidParameterException::class)
    fun handleInvalidParameterException(ex: InvalidParameterException): ResponseEntity<InternalServerException> {
        logger.error("handleInvalidParameterException - message : $ex.message")
        return ResponseEntity<InternalServerException>(
            InternalServerException("서버에서 오류가 발생했습니다. - Invalid Parameter Exception"),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun dataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<BadRequestException> {
        val message = ex.rootCause?.message
        logger.error("dataIntegrityViolationException - message : $message")
        return ResponseEntity<BadRequestException>(
            BadRequestException(
                "데이터 제약조건 오류가 발생했습니다."
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<String> {
        logger.error("handleBasicException - message : ${ex.message}")
        ex.printStackTrace()
        return ResponseEntity<String>(
            ex.message, HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(BasicException::class)
    fun handleBasicException(ex: BasicException): ResponseEntity<String> {
        logger.error("handleBasicException - message : ${ex.message}")
        return ResponseEntity<String>(
            ex.message, HttpStatus.resolve(ex.status) ?: HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException::class)
    fun invalidDataAccessApiUsageException(ex: InvalidDataAccessApiUsageException): ResponseEntity<BadRequestException> {
        logger.error("invalidDataAccessApiUsageException - message : ${ex.message}")
        ex.printStackTrace()
        return ResponseEntity<BadRequestException>(
            BadRequestException(ex.message ?: "잘못된 요청입니다."),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentExceptionHandler(ex: IllegalArgumentException): ResponseEntity<BasicException> {
        logger.error("illegalArgumentExceptionHandler - message : ${ex.message}")
        ex.printStackTrace()
        val code = if (ex.message != null) 400 else 500
        val message: String = ex.message ?: "서버에서 오류가 발생했습니다."
        return ResponseEntity<BasicException>(
            BasicException(
                code,
                message,
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<String> {
        logger.error("handle ResponseStatusException - message : $ex.message")
        return ResponseEntity<String>(
            ex.message,
            ex.status,
        )
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    @ResponseBody
    fun dtoTypeMissMatchException(exception: HttpMessageNotReadableException): ResponseEntity<BasicException> {
        exception.printStackTrace()

        val msg = when (val causeException = exception.cause) {
            is InvalidFormatException -> {
                "입력 받은 ${causeException.value} 를 ${causeException.targetType} 으로 변환중 에러가 발생했습니다."
            }

            is MissingKotlinParameterException -> {
                "Parameter is missing: ${causeException.parameter.name}"
            }

            else -> "요청을 역직렬화 하는과정에서 예외가 발생했습니다."
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            BasicException(message = msg)
        )
    }

    @ExceptionHandler(BindException::class)
    fun beanPropertyBindingResult(ex: BindException): ResponseEntity<String> {
        val codes = ArrayList<String>()

        if (ex.bindingResult.hasErrors()) {
            ex.bindingResult.fieldErrors.forEach { fieldError ->
                logger.error("filedError : ${fieldError.field}")
                codes.add(fieldError.field)
            }
        }

        return ResponseEntity<String>(
            "요청 매개변수가 잘못되었습니다. 매개변수를 확인해주세요. 개수 : ${codes.size}, 대상 : ${codes.joinToString(",")}",
            HttpStatus.BAD_REQUEST
        )
    }

    // 모든 예외를 핸들링하여 ErrorResponse 형식으로 반환한다.
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<InternalServerException> {
        logger.error("handleException - message : ${ex.message}")
        logger.error("handleException - message : ${ex.stackTraceToString()}")
        return ResponseEntity<InternalServerException>(
            InternalServerException(ex.message ?: UNKNOWN_ERROR),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    companion object {
        private val REQUEST_BODY_ERROR: String by lazy { "REQUEST-BODY와 관련된 문제가 발생했습니다." }
        private val UNKNOWN_ERROR: String by lazy { "알 수 없는 에러가 발생했습니다." }
    }
}
