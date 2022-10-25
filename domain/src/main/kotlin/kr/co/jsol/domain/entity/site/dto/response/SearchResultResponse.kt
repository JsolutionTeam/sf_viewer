package kr.co.jsol.domain.entity.site.dto.response

import kr.co.jsol.domain.entity.site.Site

data class SearchResultResponse(
    val searchResponse: SearchResponse,
    val site: Site,
)
