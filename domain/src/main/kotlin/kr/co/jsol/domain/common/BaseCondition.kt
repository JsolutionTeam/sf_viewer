package kr.co.jsol.domain.common

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

open class BaseCondition(
    @field:DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var startTime: LocalDateTime? = null,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var endTime: LocalDateTime = LocalDateTime.now().plusDays(1),
)
