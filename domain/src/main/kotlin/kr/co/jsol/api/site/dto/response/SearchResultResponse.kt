package site.dto.response

import site.Site

data class SearchResultResponse(
    val searchResponse: SearchResponse,
    val site: Site,
)
