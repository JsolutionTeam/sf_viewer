package kr.co.jsol.domain.entity.sensor.enums

enum class SensorProperty {
    RAINFALL, // 강우량
    WIND_SPEED, // 풍속
    WIND_DIRECTION, // 풍향
    SOLAR_RADIATION, // 태양광량
    TEMPERATURE, // 공기 온도
    HUMIDITY, // 공기 습도
    EARTH_TEMPERATURE, // 대지 온도
    EARTH_HUMIDITY, // 대지 습도
    ;

    /**
     * 센서 장비에서 보내준 TCP Message를 ,로 split한 후의 순서를 반환한다.
     */
    val order: Int
        /*
            01. 농가번호
            02. 고유번호 (001->센서, 002->개폐장치)
            03. 수집시간 (yyyyMMddHHmmss)

            04. 강우량
            05. 풍속
            06. 풍향
            07. 태양광량
            08. 대기작물근접온도 (공기 온도)
            09. 작물대기습도 (공기 습도)
            10. 대지온도 (땅 온도)
            11. 대지수분함수율 (땅 습도)
        */
        get() = when (this) {
            // 배열(List)라서 0부터 시작.
            RAINFALL -> 3
            WIND_SPEED -> 4
            WIND_DIRECTION -> 5
            SOLAR_RADIATION -> 6
            TEMPERATURE -> 7
            HUMIDITY -> 8
            EARTH_TEMPERATURE -> 9
            EARTH_HUMIDITY -> 10
        }
}
