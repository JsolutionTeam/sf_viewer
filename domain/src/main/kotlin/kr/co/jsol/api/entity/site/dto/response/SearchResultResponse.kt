package kr.co.jsol.api.entity.site.dto.response

import kr.co.jsol.api.entity.site.Site

data class SearchResultResponse(
    val searchResponse: SearchResponse,
    val site: Site,
)
