package kr.co.jsol.domain.entity.opening

import kr.co.jsol.domain.entity.opening.dto.OpeningResDto
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OpeningService(
    private val openingRepository: OpeningRepository,
    private val openingQuerydslRepository: OpeningQuerydslRepository,
    private val siteRepository: SiteRepository,
) {

    private val log = LoggerFactory.getLogger(OpeningService::class.java)

    @Transactional
    fun saveOpening(
        siteSeq: Long,
        rateOfOpening: Double,
        openSignal: Int,
        clientIp: String? = null,
    ): Site {
        val site = siteRepository.findById(siteSeq).orElseThrow { RuntimeException("Site not found") }
        try {
            val opening = Opening(
                rateOfOpening = rateOfOpening,
                openSignal = openSignal,
                site = site,
                ip = clientIp,
            )

            log.info("site : $site")
            log.info("opening : $opening")

            openingRepository.save(opening)
        } catch (e: Exception) {
            log.error("개폐장치 데이터 등록 중 에러발생 trace : ${e.stackTraceToString()}")
        }
        return site
    }

    fun getOpeningBySiteSeq(siteSeq: Long): OpeningResDto? {
        return openingQuerydslRepository.findInGSystemBySiteSeq(siteSeq)
    }
}
