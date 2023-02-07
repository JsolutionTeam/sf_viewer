package kr.co.jsol.domain.entity.site.dto.request

import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.util.fail

data class SiteCreateRequest(
    val id: Long?,
    val name: String?,
) {
    fun toEntity(checked: Boolean = false): Site = Site(
        id = id ?: fail("site id is null, please check"),
        name = name ?: fail("name is null, please check"),
    )
}
