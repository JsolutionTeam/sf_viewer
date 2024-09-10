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
    val siteSeq: Long,

    @Schema(description = "이산화탄소 농도 / 단위 ppm")
    var co2: Double?,

    @Schema(description = "온도 / 단위 ℃")
    var temperature: Double?,

    @Schema(description = "습도 / 단위 %")
    var relativeHumidity: Double?,

    @Schema(description = "작물 근접 온도 / 단위 ℃")
    var cropTemperature: Double?,

    @Schema(description = "작물 근접 습도 / 단위 %")
    var cropHumidity: Double?,

    @Schema(description = "일사량 / 단위 W/㎡")
    var solarRadiation: Double?,

    @Schema(description = "강우량 / 단위 mm")
    var rainfall: Double?,

    @Schema(description = "대지 습도 / 단위 ℃")
    var earthHumidity: Double?,

    @Schema(description = "대지 온도 / 단위 ℃")
    var earthTemperature: Double?,

    @Schema(description = "풍향 / 단위 ˚(각도)")
    var windDirection: Double?,

    @Schema(description = "풍속 / 단위 m/s")
    var windSpeed: Double?,

    @Schema(description = "돌풍 속도 / 단위 m/s")
    var gustSpeed: Double?,

    @Schema(description = "수분 함량 / 단위 %")
    var moistureContent: Double?,

    @Schema(description = "co2 외 데이터 수집시간", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var microRegTime: LocalDateTime?,

    @Schema(description = "co2 데이터 수집시간", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var co2RegTime: LocalDateTime?,
) {

    companion object {
        fun grouping(siteSeq: Long, co2: List<Co2Dto>, micro: List<MicroDto>): List<SummaryResponse> {
            val result = mutableListOf<SummaryResponse>()
            val co2Map = co2.groupBy { it.regTime.removeMinute() }
            val microMap = micro.groupBy { it.regTime.removeMinute() }
            val co2KeySet = co2Map.keys
            val microKeySet = microMap.keys
            val keySet = co2KeySet.union(microKeySet)
            keySet.sortedByDescending { // 날짜 최신 순으로 정렬
                it
            }.forEach { key ->
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
                        cropTemperature = microDto?.cropTemperature,
                        cropHumidity = microDto?.cropHumidity,
                        solarRadiation = microDto?.solarRadiation,
                        rainfall = microDto?.rainfall,
                        earthHumidity = microDto?.earthHumidity,
                        earthTemperature = microDto?.earthTemperature,
                        windDirection = microDto?.windDirection,
                        windSpeed = microDto?.windSpeed,
                        gustSpeed = microDto?.gustSpeed,
                        moistureContent = microDto?.moistureContent,

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
