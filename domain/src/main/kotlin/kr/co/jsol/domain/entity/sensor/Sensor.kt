package kr.co.jsol.domain.entity.sensor

import kr.co.jsol.domain.entity.site.Site
import org.hibernate.annotations.Comment
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.*

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

    // 일사량(태양광량)
    @Column(name = "solar_radiation", updatable = false)
    @Comment("일사량")
    val solarRadiation: Double = 0.0,

    // 대기 온도
    @Column(updatable = false)
    @Comment("대기 온도")
    val temperature: Double = 0.0,

    // 대기 습도
    @Column(updatable = false)
    @Comment("대기 습도")
    val humidity: Double = 0.0,

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

    override fun toString(): String {
        return "Sensor(rainfall=$rainfall, windSpeed=$windSpeed, windDirection=$windDirection, solarRadiation=$solarRadiation, temperature=$temperature, humidity=$humidity, earthTemperature=$earthTemperature, earthHumidity=$earthHumidity, site=$site, createdAt=$createdAt, id=$id, createdBy=$createdBy)"
    }
}
