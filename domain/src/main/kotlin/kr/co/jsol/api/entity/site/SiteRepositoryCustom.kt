package kr.co.jsol.api.entity.site

import kr.co.jsol.api.entity.site.dto.response.SiteResponse

interface SiteRepositoryCustom {
    fun findAllByOrderByIdAsc(): List<SiteResponse>
}
