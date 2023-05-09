package kr.co.jsol.domain.entity.sensor.dto

import kr.co.jsol.common.util.parseDouble
import kr.co.jsol.common.util.parseLong
import kr.co.jsol.domain.entity.sensor.Sensor
import kr.co.jsol.domain.entity.sensor.enums.SensorProperty
import kr.co.jsol.domain.entity.site.Site
import java.time.LocalDateTime

data class SensorTcpDto(
    val rainfall: Double = 0.0, // 강우량
    val windSpeed: Double = 0.0, // 풍속
    val windDirection: Double = 0.0, // 풍향
    val solarRadiation: Double = 0.0, // 태양광량
    val temperature: Double = 0.0, // 공기 온도
    val humidity: Double = 0.0, // 공기 습도
    val earthTemperature: Double = 0.0,
    val earthHumidity: Double = 0.0,
    val collectedAt: String,
    val siteSeq: Long = 0L,
) {

    constructor(splitMessage: List<String>) : this(
        siteSeq = splitMessage[0].parseLong(), // siteSeq는 0 고정
        collectedAt = splitMessage[2], // yyyyMMddHHmmss 형식으로 넘어옴
        rainfall = splitMessage[SensorProperty.RAINFALL.order].parseDouble(),
        windSpeed = splitMessage[SensorProperty.WIND_SPEED.order].parseDouble(),
        windDirection = splitMessage[SensorProperty.WIND_DIRECTION.order].parseDouble(),
        solarRadiation = splitMessage[SensorProperty.SOLAR_RADIATION.order].parseDouble(),
        temperature = splitMessage[SensorProperty.TEMPERATURE.order].parseDouble(),
        humidity = splitMessage[SensorProperty.HUMIDITY.order].parseDouble(),
        earthTemperature = splitMessage[SensorProperty.EARTH_TEMPERATURE.order].parseDouble(),
        earthHumidity = splitMessage[SensorProperty.EARTH_HUMIDITY.order].parseDouble(),
    )

    fun toEntity(site: Site): Sensor {
        return Sensor(
            rainfall = this.rainfall,
            windSpeed = this.windSpeed,
            windDirection = this.windDirection,
            solarRadiation = this.solarRadiation,
            temperature = this.temperature,
            humidity = this.humidity,
            earthTemperature = this.earthTemperature,
            earthHumidity = this.earthHumidity,
            createdAt = LocalDateTime.of(
                this.collectedAt.substring(0, 4).toInt(), // yyyy
                this.collectedAt.substring(4, 6).toInt(), // MM
                this.collectedAt.substring(6, 8).toInt(), // dd
                this.collectedAt.substring(8, 10).toInt(), // HH
                this.collectedAt.substring(10, 12).toInt(), // mm
                this.collectedAt.substring(12, 14).toInt(), // ss
            ),
            site = site,
        )
    }
}
