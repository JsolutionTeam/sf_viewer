package kr.co.jsol.domain.entity.site.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "데이터베이스 조회 결과")
data class ToSendRDAResponse(
    @Schema(description = "농가번호")
    val siteSeq: Long,

    @Schema(description = "농가명")
    val siteName: String,

    @Schema(description = "이산화탄소 / 단위 ppm")
    var co2: Double,

    @Schema(description = "온도 / 단위 ℃")
    var temperature: Double,

    @Schema(description = "습도 / 단위 %")
    var relativeHumidity: Double,

    @Schema(description = "작물 근접 온도 / 단위 ℃")
    var cropTemperature: Double,

    @Schema(description = "작물 근접 습도 / 단위 %")
    var cropHumidity: Double,

    @Schema(description = "일사량 / 단위 W/㎡")
    var solarRadiation: Double,

    @Schema(description = "강우량 / 단위 mm")
    var rainfall: Double,

    @Schema(description = "대지 습도 / 단위 ℃")
    var earthHumidity: Double?,

    @Schema(description = "대지 온도 / 단위 ℃")
    var earthTemperature: Double,

    @Schema(description = "풍향 / 단위 ˚(각도)")
    var windDirection: Double,

    @Schema(description = "풍속 / 단위 m/s")
    var windSpeed: Double,

    @Schema(description = "co2 외 데이터 수집시간", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var microRegTime: LocalDateTime?,

    @Schema(description = "co2 데이터 수집시간", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var co2RegTime: LocalDateTime?,
) {
    fun getTime(): LocalDateTime {
        val microRegTime: LocalDateTime = this.microRegTime ?: LocalDateTime.MIN
        val co2RegTime: LocalDateTime = this.co2RegTime ?: microRegTime

        return if (microRegTime.isAfter(co2RegTime)) {
            microRegTime
        } else
            co2RegTime
    }

    operator fun get(key: String): Double {
        return when (key) {
            "co2" -> co2 ?: 0.0
            "temperature" -> temperature ?: 0.0
            "relativeHumidity" -> relativeHumidity ?: 0.0
            "solarRadiation" -> solarRadiation ?: 0.0
            "rainfall" -> rainfall ?: 0.0
            "earthHumidity" -> earthHumidity ?: 0.0
            "earthTemperature" -> earthTemperature ?: 0.0
            "windDirection" -> windDirection ?: 0.0
            "windSpeed" -> windSpeed ?: 0.0
            else -> 0.0
        }
    }
}
