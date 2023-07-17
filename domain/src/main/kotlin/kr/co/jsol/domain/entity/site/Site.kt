package kr.co.jsol.domain.entity.site

import org.hibernate.annotations.Comment
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tb_site")
class Site(
    @Column(name = "site_nm", nullable = false)
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

    @Column(name = "site_ip", nullable = false)
    @Comment("센서 장비의 네트워크 ip")
    var ip: String = "",

    @Column(name = "api_key", nullable = false)
    @Comment("농촌진흥청 API KEY")
    var apiKey: String = "",

    // 2023-07 자동증가 값 사용이 아닌 지정할 수 있도록 요청받았음
    //  ID를 업데이트 하는 것을 추천하지 않기때문에 서비스 로직상으론 사용하지 않는 다른 ID(index)를 만들어주었음...
    //  추후 수정이 필요할 부분
    @Column(name = "site_seq")
    var seq: Long,

    @Id
    @Column(name = "site_index")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val index: Long? = null,
) {

    @Column(name = "site_ip_updated_at")
    @Comment("site ip가 업데이트 된 시간")
    var siteIpUpdatedAt: LocalDateTime? = null

    fun update(
        id: Long? = null,
        name: String? = null,
        crop: String? = null,
        location: String? = null,
        delay: Long? = null,
        apiKey: String? = null,
    ) {
        this.seq = id ?: this.seq
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
