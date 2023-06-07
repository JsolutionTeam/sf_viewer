package kr.co.jsol.domain.entity.sensor.dto

import kr.co.jsol.domain.entity.sensor.Sensor
import kr.co.jsol.domain.entity.site.Site
import org.junit.jupiter.api.Test

class SensorTcpDtoTest {

    // make dto test
    @Test
    fun makeDtoTest() {
        val sensorTcpDto = SensorTcpDto(
            listOf(
                "001",
                "001",
                "20230509124600",
                "0.00",
                "1.25",
                "90.00",
                "21.00",
                "20.00",
                "19.00",
                "20.00",
                "44.60",
            )
        )
        println(sensorTcpDto)
    }

    @Test
    fun makeDtoAndToEntityTest() {
        val sensorTcpDto = SensorTcpDto(
            listOf(
                "001",
                "001",
                "20230509124600",
                "0.00",
                "1.25",
                "90.00",
                "21.00",
                "20.00",
                "19.00",
                "20.00",
                "44.60",
            )
        )
        println(sensorTcpDto)

        val sensor: Sensor = sensorTcpDto.toEntity(
            Site(
                name = "test",
                crop = "test",
                location = "test",
            )
        )
        println(sensor)
    }
}
