package kr.co.jsol.api.entity.site.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(name = "데이터베이스 조회 결과")
data class SearchResponse(
    @Schema(name = "이산화탄소 농도 / 단위 ppm")
    val co2: Double? = 0.0,

    @Schema(name = "온도 / 단위 ℃")
    val temperature: Double? = 0.0,

    @Schema(name = "습도 / 단위 %")
    val relativeHumidity: Double? = 0.0,

    @Schema(name = "일사량 / 단위 W/㎡")
    val solarRadiation: Double? = 0.0,

    @Schema(name = "강우량 / 단위 mm")
    val rainfall: Double? = 0.0,

    @Schema(name = "지온 / 단위 ℃")
    val earthTemperature: Double? = 0.0,

    @Schema(name = "풍향 / 단위 ˚(각도)")
    val windDirection: Double? = 0.0,

    @Schema(name = "풍속 / 단위 m/s")
    val windSpeed: Double? = 0.0,

    @Schema(name = "co2 외 데이터 수집시간", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val microRegTime: LocalDateTime?,

    @Schema(name = "co2 데이터 수집시간", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val co2RegTime: LocalDateTime?,
)
