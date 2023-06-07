package kr.co.jsol.domain.entity.site

import org.springframework.data.jpa.repository.JpaRepository

interface SiteRepository : JpaRepository<Site, Long>, SiteRepositoryCustom {
    fun findByIp(ip: String): Site?

    fun findFirstByIpOrderBySiteIpUpdatedAtDesc(ip: String): Site?
}
