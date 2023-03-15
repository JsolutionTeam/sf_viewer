package kr.co.jsol.domain.entity.sensor.enums

enum class SensorProperty {
    RAINFALL,
    WIND_SPEED,
    GUST_SPEED,
    WIND_DIRECTION,
    TEMPERATURE,
    RELATIVE_HUMIDITY,
    EARTH_TEMPERATURE,
    SOLAR_RADIATION,
    COUNTER, // 내부 변수로 센서가 리셋되면 0으로 초기화 됨
    MOISTURE_CONTENT; // 사용하지 않음.


    /**
     * 센서 장비에서 보내준 TCP Message를 ,로 split한 후의 순서를 반환한다.
     */
    val order: Int
        get() = when (this) {
            // 배열(List)라서 0부터 시작.
            RAINFALL -> 2
            WIND_SPEED -> 3
            GUST_SPEED -> 4
            WIND_DIRECTION -> 5
            TEMPERATURE -> 6
            RELATIVE_HUMIDITY -> 7
            EARTH_TEMPERATURE -> 8
            SOLAR_RADIATION -> 9
            COUNTER -> 10
            MOISTURE_CONTENT -> 999
        }
}
