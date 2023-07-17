package kr.co.jsol.domain.common

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

open class BaseCondition(
    @Schema(description = "조회 시작 일자", nullable = true)
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    var startDate: LocalDate? = null,

    @Schema(description = "조회 끝 일자", nullable = true)
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    var endDate: LocalDate? = null,

    @Schema(description = "조회 시작 일시", nullable = true)
    @field:DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var startTime: LocalDateTime? = null,

    @Schema(description = "조회 끝 일시", nullable = true)
    @field:DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var endTime: LocalDateTime? = null,
) {
    val startDateTime: LocalDateTime
        get() {
            if (startDate == null) {
                startDate = LocalDate.of(1970, 1, 1)
            }
            return LocalDateTime.of(startDate, LocalTime.of(0, 0, 0))
        }

    val endDateTime: LocalDateTime
        get() {
            if (endDate == null) {
                endDate = LocalDate.now().plusDays(1)
            }
            return LocalDateTime.of(endDate, LocalTime.of(23, 59, 59))
        }

    val isDateRangeValid: Boolean
        get() {
            return startDate != null || endDate != null
        }
}
