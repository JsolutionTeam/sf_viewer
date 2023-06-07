package kr.co.jsol.domain.entity.co2

import kr.co.jsol.domain.entity.site.Site
import org.hibernate.annotations.Comment
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.ConstraintMode
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tb_co2_logger")
class Co2Logger(

    @Column(insertable = false, updatable = false)
    val co2: Double = 0.0, // 이산화탄소 농도 단위 ppm

    @Column(insertable = false, updatable = false)
    val temperature: Double = 0.0, // 대기 온도

    @Column(name = "relative_humidity", insertable = false, updatable = false)
    val relativeHumidity: Double = 0.0, // 대기 습도

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

    @ManyToOne
    @JoinColumn(
        name = "site_seq",
        foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT)
    )
    val site: Site,

    @Id
    @Column(name = "co2_logger_seq", insertable = false, updatable = false)
    val id: Long,
)
