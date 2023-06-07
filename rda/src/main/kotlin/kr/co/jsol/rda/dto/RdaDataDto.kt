package kr.co.jsol.rda.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class RdaDataDto @QueryProjection constructor(
    val id: Long, // 센서 데이터 id 번호, micro.id or sensor.id
    val siteSeq: Long, // 농장 번호
    val regTime: LocalDateTime, // 수집 시간
    val temperature: Double, // 대기 온도
    val relativeHumidity: Double, // 대기 습도
    val solarRadiation: Double, // 일사량
    val rainfall: Double, // 강우량
    val earthTemperature: Double, // 대지 온도
    var earthHumidity: Double?, // 대지 습도
    val windDirection: Double, // 풍향
    val windSpeed: Double, // 풍속
    var cropTemperature: Double?, // 작물 근접 온도 (작물과 가까운 센서에 수집된 온도)
    var cropHumidity: Double?, // 작물 근접 습도 (작물과 가까운 센서에 수집된 습도)
) {
    init {
        earthHumidity = earthHumidity ?: 0.0
        cropTemperature = cropTemperature ?: 0.0
        cropHumidity = cropHumidity ?: 0.0
    }
}
