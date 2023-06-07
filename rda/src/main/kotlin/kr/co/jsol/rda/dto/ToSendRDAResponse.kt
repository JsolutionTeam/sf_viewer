package kr.co.jsol.rda.dto

import kr.co.jsol.domain.entity.site.Site
import java.time.LocalDateTime

data class ToSendRDAResponse(
    val apiKey: String,

    val id: Long,

    val siteSeq: Long,

    val siteName: String,

    var microRegTime: LocalDateTime,

    var temperature: Double?,

    var relativeHumidity: Double?,

    var windDirection: Double?,

    var windSpeed: Double?,

    var solarRadiation: Double?,

    var rainfall: Double?,

    var earthHumidity: Double?,

    var earthTemperature: Double?,

    var cropTemperature: Double?,

    var cropHumidity: Double?,

//    var co2: Double?,

//    @Schema(description = "co2 데이터 수집시간", format = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    var co2RegTime: LocalDateTime?,
) {
    operator fun get(key: String): Any {
        return when (key) {
            "apiKey" -> apiKey
            "siteName" -> siteName
            "microRegTime" -> microRegTime
            "temperature" -> temperature ?: 0.0
            "relativeHumidity" -> relativeHumidity ?: 0.0
            "windDirection" -> windDirection ?: 0.0
            "windSpeed" -> windSpeed ?: 0.0
            "solarRadiation" -> solarRadiation ?: 0.0
            "rainfall" -> rainfall ?: 0.0
            "earthHumidity" -> earthHumidity ?: 0.0
            "earthTemperature" -> earthTemperature ?: 0.0
            "cropTemperature" -> cropTemperature ?: 0.0
            "cropHumidity" -> cropHumidity ?: 0.0
            else -> 0.0
        }
    }

    companion object {
        fun toList(site: Site, micro: List<RdaDataDto>): List<ToSendRDAResponse> {
            return micro.map {
                ToSendRDAResponse(
                    apiKey = site.apiKey,
                    id = it.id!!,
                    siteSeq = site.id!!,
                    siteName = site.name,
                    microRegTime = it.regTime,
                    temperature = it.temperature,
                    relativeHumidity = it.relativeHumidity,
                    cropTemperature = it.cropTemperature,
                    cropHumidity = it.cropHumidity,
                    solarRadiation = it.solarRadiation,
                    rainfall = it.rainfall,
                    earthHumidity = it.earthHumidity,
                    earthTemperature = it.earthTemperature,
                    windDirection = it.windDirection,
                    windSpeed = it.windSpeed,
                )
            }.toList()
        }
    }
}
