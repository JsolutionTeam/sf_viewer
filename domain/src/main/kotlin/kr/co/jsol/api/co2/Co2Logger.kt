package co2

import site.Site
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tb_co2_logger")
class Co2Logger(

    @Column(insertable = false, updatable = false)
    val co2: Double = 0.0, // 이산화탄소 농도 단위 ppm

    @Column(insertable = false, updatable = false)
    val regTime: LocalDateTime, // 정보 수집 시간
    @Column(insertable = false, updatable = false)
    val markTime: LocalDateTime, // 마크 시간

    @ManyToOne
    @JoinColumn(name = "site_seq")
    val site: Site,

    @Id
    @Column(name = "co2_logger_seq", insertable = false, updatable = false)
    val id: Long,
)