package micro.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class SearchCondition(
    val siteSeq: Long,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    val startTime: LocalDateTime?,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd ")
    val endTime: LocalDateTime?,
)
