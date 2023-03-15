package kr.co.jsol.domain.entity.co2.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class Co2Dto @QueryProjection constructor(
    val siteSeq: Long,
    val regTime: LocalDateTime,
    val co2: Double,
)
