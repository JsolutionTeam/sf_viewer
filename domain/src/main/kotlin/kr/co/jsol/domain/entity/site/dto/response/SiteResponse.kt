package kr.co.jsol.domain.entity.site.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.domain.entity.site.Site
import java.time.LocalDateTime

@Schema(description = "농가 정보")
data class SiteResponse(
    @Schema(description = "농가 번호(인덱스)")
    val id: Long,
    @Schema(description = "농가 이름")
    val crop: String,
    @Schema(description = "농가 지역")
    val location: String,
    @Schema(description = "농가 인지시스템 센서장비 데이터 수신 주기")
    val delay: Long = 1000L,
    @Schema(description = "농가 인지시스템 센서장비 마지막 수신 네트워크 ip")
    val ip: String? = null,
    @Schema(description = "농가 인지시스템 센서장비 마지막 수신 시간")
    val siteIpUpdatedAt: LocalDateTime? = null,
){
    constructor(site: Site) : this(
        id = site.id!!,
        crop = site.crop,
        location = site.location,
        delay = site.delay,
        ip = site.ip,
        siteIpUpdatedAt = site.siteIpUpdatedAt,
    )
}
