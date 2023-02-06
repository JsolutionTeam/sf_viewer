package kr.co.jsol.domain.entity.site

import kr.co.jsol.domain.entity.ingsystem.InGSystemService
import kr.co.jsol.domain.entity.site.dto.response.SearchResponse
import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import kr.co.jsol.domain.entity.site.dto.request.SearchCondition
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SiteService(
    private val siteRepository: SiteRepository,
    private val siteQuerydslRepository: SiteQuerydslRepository,
    private val inGSystemService: InGSystemService,
) {

    @Transactional(readOnly = true) // 메모리 사용 x
    fun getRealTime(condition: SearchCondition): SearchResponse {
        val realTime = siteQuerydslRepository.getRealTime(condition)
        realTime.setInGSystem(inGSystemService.getInGSystemBySiteSeq(condition.siteSeq))
        return realTime
    }

    @Transactional(readOnly = true)
    fun getSummaryBySearchCondition(condition: SearchCondition): List<SearchResponse> {
        return siteQuerydslRepository.getSummaryBySearchCondition(condition)
    }

    @Transactional(readOnly = true)
    fun getSiteList(): List<SiteResponse> {
        return siteRepository.findAllByOrderByIdAsc()
    }
}
