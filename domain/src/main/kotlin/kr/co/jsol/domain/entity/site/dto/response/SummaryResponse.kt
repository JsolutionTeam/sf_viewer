package kr.co.jsol.domain.entity.site.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.common.util.removeMinute
import kr.co.jsol.domain.entity.co2.dto.Co2Dto
import kr.co.jsol.domain.entity.micro.dto.MicroDto
import java.time.LocalDateTime

@Schema(description = "데이터베이스 조회 결과")
data class SummaryResponse(
    @Schema(description = "site id")
    val siteSeq: Long = 0L,

    @Schema(description = "이산화탄소 농도 / 단위 ppm")
    var co2: Double? = 0.0,

    @Schema(description = "온도 / 단위 ℃")
    var temperature: Double? = 0.0,

    @Schema(description = "습도 / 단위 %")
    var relativeHumidity: Double? = 0.0,

    @Schema(description = "일사량 / 단위 W/㎡")
    var solarRadiation: Double? = 0.0,

    @Schema(description = "강우량 / 단위 mm")
    var rainfall: Double? = 0.0,

    @Schema(description = "지온 / 단위 ℃")
    var earthTemperature: Double? = 0.0,

    @Schema(description = "풍향 / 단위 ˚(각도)")
    var windDirection: Double? = 0.0,

    @Schema(description = "풍속 / 단위 m/s")
    var windSpeed: Double? = 0.0,

    @Schema(description = "co2 외 데이터 수집시간", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var microRegTime: LocalDateTime? = null,

    @Schema(description = "co2 데이터 수집시간", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var co2RegTime: LocalDateTime? = null,
) {

    fun setCo2Info(dto: Co2Dto) {
        this.co2 = dto.co2
        this.co2RegTime = dto.regTime
    }

    fun setMicro(dto: MicroDto) {
        temperature = dto.temperature
        relativeHumidity = dto.relativeHumidity
        solarRadiation = dto.solarRadiation
        rainfall = dto.rainfall
        earthTemperature = dto.earthTemperature
        windDirection = dto.windDirection
        windSpeed = dto.windSpeed
        this.microRegTime = dto.regTime
    }

    companion object {

        fun of(siteSeq: Long, co2: List<Co2Dto>, micro: List<MicroDto>): List<SummaryResponse> {
            val result = mutableListOf<SummaryResponse>()
            val co2Map = co2.groupBy { it.regTime.removeMinute() }
            val microMap = micro.groupBy { it.regTime.removeMinute() }
            val co2KeySet = co2Map.keys
            val microKeySet = microMap.keys
            val keySet = co2KeySet.union(microKeySet)
            keySet.forEach { key ->
                val co2Dto = co2Map[key]?.firstOrNull()
                val microDto = microMap[key]?.firstOrNull()
                result.add(
                    SummaryResponse(
                        // site
                        siteSeq = siteSeq,

                        // co2
                        co2 = co2Dto?.co2,

                        // micro
                        temperature = microDto?.temperature,
                        relativeHumidity = microDto?.relativeHumidity,
                        solarRadiation = microDto?.solarRadiation,
                        rainfall = microDto?.rainfall,
                        earthTemperature = microDto?.earthTemperature,
                        windDirection = microDto?.windDirection,
                        windSpeed = microDto?.windSpeed,

                        // regtime
                        microRegTime = microDto?.regTime?.removeMinute(),
                        co2RegTime = co2Dto?.regTime?.removeMinute(),
                    )
                )
            }
            return result
        }
    }
}
