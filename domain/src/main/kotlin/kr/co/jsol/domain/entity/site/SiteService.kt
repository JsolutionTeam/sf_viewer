package kr.co.jsol.domain.entity.site

import kr.co.jsol.domain.entity.opening.OpeningService
import kr.co.jsol.domain.entity.opening.dto.OpeningResDto
import kr.co.jsol.domain.entity.site.dto.response.SummaryResponse
import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import kr.co.jsol.domain.entity.site.dto.request.SearchCondition
import kr.co.jsol.domain.entity.site.dto.request.SiteCreateRequest
import kr.co.jsol.domain.entity.site.dto.request.SiteUpdateRequest
import kr.co.jsol.domain.entity.site.dto.response.RealTimeResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SiteService(
    private val siteRepository: SiteRepository,
    private val siteQuerydslRepository: SiteQuerydslRepository,
    private val openingService: OpeningService,
) {
    // 기본 delay
    @Value("\${inGSystem.message.default-delay:60}")
    private val defaultDelay: Long = 60

    fun getById(siteSeq: Long): SiteResponse {
        val optional = siteRepository.findById(siteSeq)
        if(optional.isEmpty) {
            throw IllegalArgumentException("존재하지 않는 농장 번호입니다.")
        }
        return SiteResponse.of(optional.get())
    }

    fun getDelayByIp(ip: String): Long {
        val site = siteRepository.findFirstByIpOrderBySiteIpUpdatedAtDesc(ip) ?: return defaultDelay
        return site.delay
    }

    fun saveSite(siteCreateRequest: SiteCreateRequest): Long {
        val siteSeq = siteCreateRequest.id
        if(siteSeq == null || siteRepository.existsById(siteSeq)) {
            throw IllegalArgumentException("이미 존재하는 농장 번호입니다.")
        }
        val site = siteRepository.save(siteCreateRequest.toEntity())
        return site.id
    }

    fun isExistSiteSeq(siteSeq: Long): Boolean {
        return siteRepository.existsById(siteSeq)
    }

    fun getRealTime(condition: SearchCondition): RealTimeResponse {
        val realTime = siteQuerydslRepository.getRealTime(condition)
        realTime.setOpening(openingService.getOpeningBySiteSeq(condition.siteSeq))
        return realTime
    }

    fun getSummaryBySearchCondition(condition: SearchCondition): List<SummaryResponse> {
        return siteQuerydslRepository.getSummaryBySearchCondition(condition)
    }

    fun getDoorSummaryBySearchCondition(condition: SearchCondition): List<OpeningResDto> {
        return siteQuerydslRepository.getDoorSummaryBySearchCondition(condition)
    }

    fun getSiteList(): List<SiteResponse> {
        return siteRepository.findAllByOrderByIdAsc()
    }

    fun updateSite(siteSeq: Long, siteUpdateRequest: SiteUpdateRequest): Long {
        val site = siteRepository.findById(siteSeq).orElseThrow { IllegalArgumentException("해당 농장이 존재하지 않습니다.") }
        site.update(
            name = siteUpdateRequest.name,
            delay = siteUpdateRequest.delay,
            ip = siteUpdateRequest.ip,
        )
        siteRepository.save(site)
        return siteSeq
    }

    fun deleteSite(siteSeq: Long) {
        siteRepository.deleteById(siteSeq)
    }
}
