package kr.co.jsol.domain.entity.site.dto.response

import kr.co.jsol.domain.entity.site.Site

data class SiteResponse(
    val id: Long,
    val name: String,
){
    companion object {
        fun of(site: Site): SiteResponse {
            return SiteResponse(
                id = site.id,
                name = site.name,
            )
        }
    }
}
