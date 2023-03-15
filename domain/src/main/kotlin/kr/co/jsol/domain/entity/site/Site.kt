package kr.co.jsol.domain.entity.site

import org.hibernate.annotations.Comment
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tb_site")
class Site(
    @Column(name = "site_nm")
    var name: String = "",

    @Column(name = "logger_delay", nullable = false)
    var delay: Long = 1000L,

    // site ip -> 센서 장비의 네트워크 ip
    @Column(name = "site_ip")
    var ip: String = "",

    @Id
    @Column(name = "site_seq", updatable = false)
    val id: Long,
) {

    @Column(name = "site_ip_updated_at")
    @Comment("site ip가 업데이트 된 시간")
    var siteIpUpdatedAt: LocalDateTime? = null

    fun update(
        name: String? = null,
        delay: Long? = null,
        ip: String? = null,
    ) {
        this.name = name ?: this.name
        this.delay = delay ?: this.delay
        this.ip = ip ?: this.ip
    }

    fun updateLastSensorIp(ip : String){
        this.ip = ip
        this.siteIpUpdatedAt = LocalDateTime.now()
    }
}
