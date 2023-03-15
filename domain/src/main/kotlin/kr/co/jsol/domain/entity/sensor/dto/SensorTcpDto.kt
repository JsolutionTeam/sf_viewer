package kr.co.jsol.domain.entity.sensor.dto

import kr.co.jsol.common.util.parseDouble
import kr.co.jsol.common.util.parseLong
import kr.co.jsol.domain.entity.sensor.Sensor
import kr.co.jsol.domain.entity.sensor.enums.SensorProperty
import kr.co.jsol.domain.entity.site.Site

data class SensorTcpDto(
    val rainfall: Double = 0.0,
    val windSpeed: Double = 0.0,

    val gustSpeed: Double = 0.0,

    val windDirection: Double = 0.0,

    val temperature: Double = 0.0,

    val relativeHumidity: Double = 0.0,

    val earthTemperature: Double = 0.0,

    val solarRadiation: Double = 0.0,

    // 내부 변수로 사용, 데이터를 보낼때마다 1씩 증가하며 기기가 리셋되면 0으로 초기화 함
    val counter: Double = 0.0,

    // 현재 사용하지 않음
    val moistureContent: Double = 0.0,

    val siteSeq: Long = 0L,
) {


    fun parseSplitMessageToSensorTcpDto(splitMessage: List<String>): SensorTcpDto {
        return SensorTcpDto(
            siteSeq = splitMessage[0].parseLong(), // siteSeq는 0 고정
            rainfall = splitMessage[SensorProperty.RAINFALL.order].parseDouble(),
            windSpeed = splitMessage[SensorProperty.WIND_SPEED.order].parseDouble(),
            gustSpeed = splitMessage[SensorProperty.GUST_SPEED.order].parseDouble(),
            windDirection = splitMessage[SensorProperty.WIND_DIRECTION.order].parseDouble(),
            temperature = splitMessage[SensorProperty.TEMPERATURE.order].parseDouble(),
            relativeHumidity = splitMessage[SensorProperty.RELATIVE_HUMIDITY.order].parseDouble(),
            earthTemperature = splitMessage[SensorProperty.EARTH_TEMPERATURE.order].parseDouble(),
            solarRadiation = splitMessage[SensorProperty.SOLAR_RADIATION.order].parseDouble(),
            counter = splitMessage[SensorProperty.COUNTER.order].parseDouble(),
//            moistureContent = splitMessage[SensorProperty.MOISTURE_CONTENT.order].parseDouble(),
        )
    }

    fun toEntity(site: Site): Sensor {
        return Sensor(
            rainfall = this.rainfall,
            windSpeed = this.windSpeed,
            gustSpeed = this.gustSpeed,
            windDirection = this.windDirection,
            temperature = this.temperature,
            relativeHumidity = this.relativeHumidity,
            earthTemperature = this.earthTemperature,
            solarRadiation = this.solarRadiation,
            moistureContent = this.moistureContent,
            counter = this.counter,
            site = site,
        )
    }
}
