package kr.co.jsol.domain.entity.micro.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class MicroDto  @QueryProjection constructor(
    val siteSeq: Long,
    val regTime : LocalDateTime,
    val temperature: Double,
    val relativeHumidity: Double,
    val solarRadiation: Double,
    val rainfall: Double,
    val earthTemperature: Double,
    val windDirection: Double,
    val windSpeed: Double,
)
