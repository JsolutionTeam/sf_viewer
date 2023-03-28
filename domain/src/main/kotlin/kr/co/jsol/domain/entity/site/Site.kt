package kr.co.jsol.domain.entity.site

import org.hibernate.annotations.Comment
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tb_site")
class Site(
    @Column(name = "site_nm")
    var crop: String,

    @Column(name = "site_location")
    var location: String,

    @Column(name = "logger_delay", nullable = false)
    var delay: Long = 1000L,

    // site ip -> 센서 장비의 네트워크 ip
    @Column(name = "site_ip")
    var ip: String = "",

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_seq", updatable = false)
    val id: Long? = null,
) {

    @Column(name = "site_ip_updated_at")
    @Comment("site ip가 업데이트 된 시간")
    var siteIpUpdatedAt: LocalDateTime? = null

    fun update(
        crop: String? = null,
        location: String? = null,
        delay: Long? = null,
        ip: String? = null,
    ) {
        this.crop = crop ?: this.crop
        this.location = location ?: this.location
        this.delay = delay ?: this.delay
        this.ip = ip ?: this.ip
    }

    fun updateLastSensorIp(ip : String){
        this.ip = ip
        this.siteIpUpdatedAt = LocalDateTime.now()
    }
}
