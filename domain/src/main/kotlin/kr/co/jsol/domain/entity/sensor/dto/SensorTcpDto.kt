package kr.co.jsol.domain.entity.sensor.dto

import kr.co.jsol.common.util.parseDouble
import kr.co.jsol.common.util.parseLong
import kr.co.jsol.common.util.safeDoubleValue
import kr.co.jsol.common.util.safeLocalDateTimeValue
import kr.co.jsol.domain.entity.sensor.Sensor
import kr.co.jsol.domain.entity.sensor.enums.SensorProperty
import kr.co.jsol.domain.entity.site.Site
import java.time.LocalDateTime

data class SensorTcpDto(
    val rainfall: Double = 0.0, // 강우량
    val windSpeed: Double = 0.0, // 풍속
    val windDirection: Double = 0.0, // 풍향
    val temperature: Double = 0.0, // 대기 온도
    val humidity: Double = 0.0, // 대기 습도
    val solarRadiation: Double = 0.0, // 태양광량
    val cropTemperature: Double = 0.0, // 작물 근접 온도 (작물과 가까운 센서에 수집된 온도)
    val cropHumidity: Double = 0.0, // 작물 근접 습도 (작물과 가까운 센서에 수집된 습도)
    val earthTemperature: Double = 0.0, // 대지 온도
    val earthHumidity: Double = 0.0, // 대지 습도
    val collectedAt: String,
    val siteSeq: Long = 0L,
) {

    constructor(splitMessage: List<String>) : this(
        siteSeq = splitMessage[0].parseLong(), // siteSeq는 0 고정
        collectedAt = splitMessage[2], // yyyyMMddHHmmss 형식으로 넘어옴
        rainfall = splitMessage[SensorProperty.RAINFALL.order].parseDouble(),
        windSpeed = splitMessage[SensorProperty.WIND_SPEED.order].parseDouble(),
        windDirection = splitMessage[SensorProperty.WIND_DIRECTION.order].parseDouble(),
        temperature = splitMessage[SensorProperty.TEMPERATURE.order].parseDouble(),
        humidity = splitMessage[SensorProperty.HUMIDITY.order].parseDouble(),
        solarRadiation = splitMessage[SensorProperty.SOLAR_RADIATION.order].parseDouble(),
        cropTemperature = splitMessage[SensorProperty.CROP_TEMPERATURE.order].parseDouble(),
        cropHumidity = splitMessage[SensorProperty.CROP_HUMIDITY.order].parseDouble(),
        earthTemperature = splitMessage[SensorProperty.EARTH_TEMPERATURE.order].parseDouble(),
        earthHumidity = splitMessage[SensorProperty.EARTH_HUMIDITY.order].parseDouble(),
    )

    fun toEntity(site: Site): Sensor {
        return Sensor(
            rainfall = safeDoubleValue { this.rainfall },
            windSpeed = safeDoubleValue { this.windSpeed },
            windDirection = safeDoubleValue { this.windDirection },
            temperature = safeDoubleValue { this.temperature },
            humidity = safeDoubleValue { this.humidity },
            solarRadiation = safeDoubleValue { this.solarRadiation },
            cropTemperature = safeDoubleValue { this.cropTemperature },
            cropHumidity = safeDoubleValue { this.cropHumidity },
            earthTemperature = safeDoubleValue { this.earthTemperature },
            earthHumidity = safeDoubleValue { this.earthHumidity },
            createdAt = safeLocalDateTimeValue {
                LocalDateTime.of(
                    this.collectedAt.substring(0, 4).toInt(), // yyyy
                    this.collectedAt.substring(4, 6).toInt(), // MM
                    this.collectedAt.substring(6, 8).toInt(), // dd
                    this.collectedAt.substring(8, 10).toInt(), // HH
                    this.collectedAt.substring(10, 12).toInt(), // mm
                    this.collectedAt.substring(12, 14).toInt(), // ss
                )
            },
            site = site,
        )
    }
}
