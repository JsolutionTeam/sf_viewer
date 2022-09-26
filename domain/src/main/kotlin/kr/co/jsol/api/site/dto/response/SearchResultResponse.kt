package kr.co.jsol.api.site.dto.response

import kr.co.jsol.api.site.Site

data class SearchResultResponse(
    val searchResponse: SearchResponse,
    val site: Site,
)
