package kr.co.jsol.domain.entity.opening

import kr.co.jsol.domain.entity.site.Site
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.ConstraintMode
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tb_opening")
class Opening(

    // 개폐장치 움직인 정도 값
    @Column(name = "rate_of_opening")
    val rateOfOpening: Double = 0.0,

    // 개폐장치 작동 시그널 값 -1: 역방향, 0: 정지, 1: 정방향
    @Column(name = "open_signal")
    val openSignal: Int = 0,

    // 정보 수집 시간
    @Column(name = "reg_dtm")
    val regTime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "machine_id")
    val machineId: Long = 0,

    @Column(name = "ip")
    val ip: String? = null,

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
    )
    @JoinColumn(
        name = "site_seq",
        foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT)
    )
    var site: Site? = null,

    @Id
    @Column(name = "opening_seq", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
    override fun toString(): String {
        return "Opening(id=$id, rateOfOpening=$rateOfOpening, openSignal=$openSignal, regTime=$regTime, machineId=$machineId, site=$site)"
    }
}
