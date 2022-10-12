package kr.co.jsol.api.entity.site

import kr.co.jsol.api.entity.site.dto.response.SearchResponse
import kr.co.jsol.api.entity.site.dto.response.SiteResponse
import kr.co.jsol.api.entity.site.dto.request.SearchCondition
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SiteService(
    private val siteRepository: SiteRepository,
    private val siteQuerydslRepository: SiteQuerydslRepository,
) {

    @Transactional(readOnly = true) // 메모리 사용 x
    fun getRealTime(condition: SearchCondition): List<SearchResponse> {
        return siteQuerydslRepository.getRealTime(condition)
    }

    @Transactional(readOnly = true)
    fun getByRegTime(condition: SearchCondition): List<SearchResponse> {
        return siteQuerydslRepository.getMicroList(condition)
    }

    @Transactional(readOnly = true)
    fun getSiteList(): List<SiteResponse> {
        return siteRepository.findAllByOrderByIdAsc()
    }
}
