package kr.co.jsol.domain.entity.site

import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import org.springframework.data.jpa.repository.JpaRepository

interface SiteRepository : JpaRepository<Site, Long> {
    fun findByIp(ip: String): Site?
    fun findBySeq(siteSeq: Long): Site?

//    fun findAllByOrderBySeqAsc(): List<SiteResponse>
    fun findAllByOrderBySeqAsc(): List<Site>

    fun findFirstByIpOrderBySiteIpUpdatedAtDesc(ip: String): Site?
}
