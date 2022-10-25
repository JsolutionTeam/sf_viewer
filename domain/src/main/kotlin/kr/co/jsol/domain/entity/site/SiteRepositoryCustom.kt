package kr.co.jsol.domain.entity.site

import kr.co.jsol.domain.entity.site.dto.response.SiteResponse

interface SiteRepositoryCustom {
    fun findAllByOrderByIdAsc(): List<SiteResponse>
}
