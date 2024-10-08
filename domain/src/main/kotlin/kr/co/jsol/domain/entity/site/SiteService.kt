package kr.co.jsol.domain.entity.site

import kr.co.jsol.domain.entity.opening.OpeningService
import kr.co.jsol.domain.entity.opening.dto.OpeningResDto
import kr.co.jsol.domain.entity.site.dto.request.SiteSearchCondition
import kr.co.jsol.domain.entity.site.dto.response.RealTimeResponse
import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import kr.co.jsol.domain.entity.site.dto.response.SummaryResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SiteService(
    private val siteRepository: SiteRepository,
    private val siteQuerydslRepository: SiteQuerydslRepository,
    private val openingService: OpeningService,
) {
    private val log = LoggerFactory.getLogger(SiteService::class.java)

    // 기본 delay
    @Value("\${inGSystem.message.default-delay:60}")
    private val defaultDelay: Long = 60

    fun isExistSiteSeq(siteSeq: Long) = siteRepository.existsById(siteSeq)

    fun findByIp(ip: String): Site? {
        return siteRepository.findFirstByIpOrderBySiteIpUpdatedAtDesc(ip)
    }

    fun getDelayByIp(ip: String): Long {
        val site = findByIp(ip) ?: return defaultDelay
        return site.delay
    }

    fun getRealTime(condition: SiteSearchCondition): RealTimeResponse {
        val realTime = siteQuerydslRepository.getRealTime(condition)
        realTime.setOpening(openingService.getOpeningBySiteSeq(condition.siteSeq))
        return realTime
    }

    fun getSummaryBySearchCondition(condition: SiteSearchCondition): List<SummaryResponse> {
        return siteQuerydslRepository.getSummaryBySearchCondition(condition)
    }

    fun getDoorSummaryBySearchCondition(condition: SiteSearchCondition): List<OpeningResDto> {
        return siteQuerydslRepository.getDoorSummaryBySearchCondition(condition)
    }

    fun list(): List<SiteResponse> {
//        return siteRepository.findAllByOrderByIdAsc()
        log.info("농가 전체 조회")
        return siteQuerydslRepository.findAll().map {
            SiteResponse(it)
        }
    }

    fun isAbleToSendRda(siteSeq: Long): Boolean {
        val site = siteRepository.findById(siteSeq).orElseThrow { throw Exception("site not found") }
        return site.isAbleToSendRda()
    }
}
