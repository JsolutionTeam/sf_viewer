package kr.co.jsol.domain.entity.sensor.enums

/*
    04. 강우량
    05. 풍속
    06. 풍향
    07. 대기온도
    08. 대기습도
    09. 태양광량
    10. 작물근접온도 (작물과 가까운 센서에 수집된 온도)
    11. 작물근접습도 (작물과 가까운 센서에 수집된 습도)
    12. 대지온도 (땅 온도)
    13. 대지수분함수율 (땅 습도)
*/
enum class SensorProperty {
    RAINFALL, // 강우량
    WIND_SPEED, // 풍속
    WIND_DIRECTION, // 풍향
    TEMPERATURE, // 대기 온도
    HUMIDITY, // 대기 습도
    SOLAR_RADIATION, // 태양광량
    CROP_TEMPERATURE, // 작물 근접 온도
    CROP_HUMIDITY, // 작물 근접 습도
    EARTH_TEMPERATURE, // 대지 온도
    EARTH_HUMIDITY, // 대지 습도
    ;

    /**
     * 센서 장비에서 보내준 TCP Message를 ,로 split한 후의 순서를 반환한다.
     */
    val order: Int
        get() = when (this) {
            // 배열(List)라서 0부터 시작.
            RAINFALL -> 3
            WIND_SPEED -> 4
            WIND_DIRECTION -> 5
            TEMPERATURE -> 6
            HUMIDITY -> 7
            SOLAR_RADIATION -> 8
            CROP_TEMPERATURE -> 9
            CROP_HUMIDITY -> 10
            EARTH_TEMPERATURE -> 11
            EARTH_HUMIDITY -> 12
        }
}
