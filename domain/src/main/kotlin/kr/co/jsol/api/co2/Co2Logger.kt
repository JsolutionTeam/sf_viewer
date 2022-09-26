package kr.co.jsol.api.co2

import kr.co.jsol.api.site.Site
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tb_co2_logger")
class Co2Logger(

    @Column(insertable = false, updatable = false)
    val co2: Double = 0.0, // 이산화탄소 농도 단위 ppm

    // 정보 수집 시간
    @Column(name = "reg_dtm", insertable = false, updatable = false)
    val regTime: LocalDateTime,

    // 마크 시간
    @Column(name = "mark_tm", insertable = false, updatable = false)
    val markTime: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "site_seq")
    val site: Site,

    @Id
    @Column(name = "co2_logger_seq", insertable = false, updatable = false)
    val id: Long,
)
