package kr.co.jsol.domain.entity.sensor

import kr.co.jsol.domain.entity.site.Site
import org.hibernate.annotations.Comment
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.*

/*
    데이터 포맷 변경 이력
        2023-05-16 데이터 포맷 : *농가번호,고유코드, 수집시간, 강우, 풍속, 풍향, 대기온도,대기습도, 태양광량, 작물근접온도, 작물대기습도, 대지온도,대지수분함수율#

 */
@Entity
@Table(
    name = "tb_sensor",
)
class Sensor(

    // 강우량
    @Column(name = "rainfall", updatable = false)
    @Comment("강우량")
    val rainfall: Double = 0.0,

    // 풍속
    @Column(name = "wind_speed", updatable = false)
    @Comment("풍속")
    val windSpeed: Double = 0.0,

    // 풍향
    @Column(name = "wind_direction", updatable = false)
    @Comment("풍향")
    val windDirection: Double = 0.0,

    // 대기 온도
    @Column(name = "temperature", updatable = false)
    @Comment("대기 온도")
    val temperature: Double = 0.0,

    // 대기 습도
    @Column(name = "humidity", updatable = false)
    @Comment("대기 습도")
    val humidity: Double = 0.0,

    // 일사량(태양광량)
    @Column(name = "solar_radiation", updatable = false)
    @Comment("일사량")
    val solarRadiation: Double = 0.0,

    // 작물 근접 온도 (작물과 가까운 센서에 수집된 온도)
    @Column(name = "crop_temperature", updatable = false)
    @Comment("대기 온도")
    val cropTemperature: Double = 0.0,

    // 작물 근접 습도 (작물과 가까운 센서에 수집된 습도)
    @Column(name = "crop_humidity", updatable = false)
    @Comment("대기 습도")
    val cropHumidity: Double = 0.0,

    // 대지 온도
    @Column(name = "earth_temperature", updatable = false)
    @Comment("대지 온도")
    val earthTemperature: Double = 0.0,

    // 대지 습도
    @Column(name = "earth_humidity", updatable = false)
    @Comment("대지 습도")
    val earthHumidity: Double = 0.0,

    // 농장 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "site_seq",
        foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT)
    )
    @Comment("농가 정보")
    val site: Site,

    // 정보 수집 시간
    @field:CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_id", updatable = false)
    @Comment("기본키")
    val id: Long? = null,
) {

    // 정보 생성자 정보-> ip 기입
    var createdBy: String? = null

    fun create(createdBy: String?) {
        this.createdBy = createdBy
    }

    override fun toString(): String =
        "Sensor(rainfall=$rainfall, windSpeed=$windSpeed, windDirection=$windDirection, temperature=$temperature, humidity=$humidity, solarRadiation=$solarRadiation, cropTemperature=$cropTemperature, cropHumidity=$cropHumidity, earthTemperature=$earthTemperature, earthHumidity=$earthHumidity, site=$site, createdAt=$createdAt, id=$id, createdBy=$createdBy)"
}
