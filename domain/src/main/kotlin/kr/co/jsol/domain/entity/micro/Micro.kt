package kr.co.jsol.domain.entity.micro

import kr.co.jsol.domain.entity.site.Site
import org.hibernate.annotations.Comment
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.ConstraintMode
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ForeignKey
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tb_micro_station")
class Micro(
    // 온도
    @Column(insertable = false, updatable = false)
    val temperature: Double = 0.0,

    // 대기 습도
    @Column(name = "relative_humidity", insertable = false, updatable = false)
    val relativeHumidity: Double = 0.0,

    // 일사량
    @Column(name = "solar_radiation", insertable = false, updatable = false)
    val solarRadiation: Double = 0.0,

    // 강우량
    @Column(insertable = false, updatable = false)
    val rainfall: Double = 0.0,

    // 지온
    @Column(name = "earth_temperature", insertable = false, updatable = false)
    val earthTemperature: Double = 0.0,

    // 풍향
    @Column(name = "wind_direction", insertable = false, updatable = false)
    val windDirection: Double = 0.0,

    // 풍속
    @Column(name = "wind_speed", insertable = false, updatable = false)
    val windSpeed: Double = 0.0,

    // 정보 수집 시간
    @Column(name = "reg_dtm", insertable = false, updatable = false)
    val regTime: LocalDateTime,

    // 마크 시간
    @Column(name = "mark_tm", insertable = false, updatable = false)
    val markTime: LocalDateTime,

    // 농촌진흥청 데이터 전송 여부
    @Column(name = "is_send", nullable = false)
    @Comment("농촌진흥청 데이터 전송 여부")
    val isSend: Boolean = false,

    // 농장 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "site_seq",
        foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT)
    )
    val site: Site,

    // 기본키
    @Id
    @Column(name = "micro_station_seq", insertable = false, updatable = false)
    val id: Long,
)
