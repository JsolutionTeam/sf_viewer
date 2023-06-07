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
    @Comment("농가 이름")
    var name: String,

    @Column(name = "site_crop", nullable = false)
    @Comment("농가 재배 작물 ex 감자")
    var crop: String,

    @Column(name = "site_location", nullable = false)
    @Comment("센서가 설치된 위치 ex 김제")
    var location: String,

    @Column(name = "logger_delay", nullable = false)
    @Comment("센서 데이터를 받는 주기(초)")
    var delay: Long = 60L,

    // site ip -> 센서 장비의 네트워크 ip
    @Column(name = "site_ip")
    var ip: String = "",

    @Column(name = "api_key")
    var apiKey: String = "",

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_seq", updatable = false)
    val id: Long? = null,
) {

    @Column(name = "site_ip_updated_at")
    @Comment("site ip가 업데이트 된 시간")
    var siteIpUpdatedAt: LocalDateTime? = null

    fun update(
        name: String? = null,
        crop: String? = null,
        location: String? = null,
        delay: Long? = null,
        apiKey: String? = null,
    ) {
        this.name = name ?: this.name
        this.location = location ?: this.location
        this.crop = crop ?: this.crop
        this.delay = delay ?: this.delay
        this.apiKey = apiKey ?: this.apiKey
    }

    fun updateLastSensorIp(ip: String) {
        this.ip = ip
        this.siteIpUpdatedAt = LocalDateTime.now()
    }

    fun isAbleToSendRda(): Boolean {
        return this.apiKey.isNotEmpty()
    }
}
