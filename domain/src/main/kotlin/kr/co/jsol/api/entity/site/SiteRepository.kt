package kr.co.jsol.api.entity.site

import org.springframework.data.jpa.repository.JpaRepository

interface SiteRepository : JpaRepository<Site, Long>, SiteRepositoryCustom
