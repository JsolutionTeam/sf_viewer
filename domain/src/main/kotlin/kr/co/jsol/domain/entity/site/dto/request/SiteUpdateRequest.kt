package kr.co.jsol.domain.entity.site.dto.request

import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.util.fail

data class SiteUpdateRequest(
    val name: String?,
)