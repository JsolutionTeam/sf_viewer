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
    @Column(updatable = false)
    @Comment("강우량")
    val rainfall: Double = 0.0,

    // 풍속
    @Column(name = "wind_speed", updatable = false)
    @Comment("풍속")
    val windSpeed: Double = 0.0,

    // 돌풍속도
    @Column(name = "gust_spped", updatable = false)
    @Comment("돌풍속도")
    val gustSpeed: Double = 0.0,

    // 풍향
    @Column(name = "wind_direction", updatable = false)
    @Comment("풍향")
    val windDirection: Double = 0.0,

    // 대기 온도
    @Column(updatable = false)
    @Comment("대기 온도")
    val temperature: Double = 0.0,

    // 대기 습도
    @Column(name = "relative_humidity", updatable = false)
    @Comment("대기 습도")
    val relativeHumidity: Double = 0.0,

    // 지온
    @Column(name = "earth_temperature", updatable = false)
    @Comment("지온")
    val earthTemperature: Double = 0.0,

    // 일사량
    @Column(name = "solar_radiation", updatable = false)
    @Comment("일사량")
    val solarRadiation: Double = 0.0,

    // 수분 함량
    @Column(name = "moisture_content", updatable = false)
    @Comment("수분 함량")
    val moistureContent: Double = 0.0,

    // 내부변수인 카운터 값 -> 기기가 데이터를 보낼때마다 1씩 증가하며, 기기가 접속 실패 등으로 실패하면 0으로 리셋
    @Column(name = "counter", updatable = false)
    @Comment("기기 내부 카운터 변수")
    val counter: Double = 0.0,

    // 농장 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "site_seq",
        foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT)
    )
    @Comment("농가 정보")
    val site: Site,

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_id", updatable = false)
    @Comment("기본키")
    val id: Long? = null,
) {

    // 정보 수집 시간
    @field:CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now()

    // 정보 생성자 정보-> ip 기입
    var createdBy: String? = null

    fun create(createdBy: String?) {
        this.createdBy = createdBy
    }
}
