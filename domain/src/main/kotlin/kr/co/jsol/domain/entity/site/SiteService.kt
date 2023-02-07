package kr.co.jsol.domain.entity.site

import kr.co.jsol.domain.entity.ingsystem.InGSystemService
import kr.co.jsol.domain.entity.ingsystem.dto.InGSystemDto
import kr.co.jsol.domain.entity.site.dto.response.SummaryResponse
import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import kr.co.jsol.domain.entity.site.dto.request.SearchCondition
import kr.co.jsol.domain.entity.site.dto.request.SiteCreateRequest
import kr.co.jsol.domain.entity.site.dto.request.SiteUpdateRequest
import kr.co.jsol.domain.entity.site.dto.response.RealTimeResponse
import org.springframework.stereotype.Service

@Service
class SiteService(
    private val siteRepository: SiteRepository,
    private val siteQuerydslRepository: SiteQuerydslRepository,
    private val inGSystemService: InGSystemService,
) {

    fun saveSite(siteCreateRequest: SiteCreateRequest): Long {
        val site = siteRepository.save(siteCreateRequest.toEntity())
        return site.id
    }

    fun getRealTime(condition: SearchCondition): RealTimeResponse {
        val realTime = siteQuerydslRepository.getRealTime(condition)
        realTime.setInGSystem(inGSystemService.getInGSystemBySiteSeq(condition.siteSeq))
        return realTime
    }

    fun getSummaryBySearchCondition(condition: SearchCondition): List<SummaryResponse> {
        return siteQuerydslRepository.getSummaryBySearchCondition(condition)
    }

    fun getDoorSummaryBySearchCondition(condition: SearchCondition): List<InGSystemDto> {
        return siteQuerydslRepository.getDoorSummaryBySearchCondition(condition)
    }

    fun getSiteList(): List<SiteResponse> {
        return siteRepository.findAllByOrderByIdAsc()
    }

    fun updateSite(siteSeq: Long, siteUpdateRequest: SiteUpdateRequest): Long {
        val site = siteRepository.findById(siteSeq).orElseThrow { IllegalArgumentException("해당 농장이 존재하지 않습니다.") }
        site.update(siteUpdateRequest)
        siteRepository.save(site)
        return siteSeq
    }

    fun deleteSite(siteSeq: Long) {
        siteRepository.deleteById(siteSeq)
    }
}
