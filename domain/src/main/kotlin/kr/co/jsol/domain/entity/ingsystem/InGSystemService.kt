package kr.co.jsol.domain.entity.ingsystem

import kr.co.jsol.domain.entity.site.SiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException

@Service
class InGSystemService(
    private val inGSystemRepository: InGSystemRepository,
    private val siteRepository: SiteRepository,
) {

    // trasnactional이 없으면 site가 조회 안되고 생성됨 왜지?
    @Transactional
    fun saveInGSystem(
        siteSeq: Long,
        rateOfOpening: Double,
        openSignal: Int,
        clientIp: String? = null,
    ): String {
        try {
            val site = siteRepository.findById(siteSeq).orElseThrow { RuntimeException("Site not found") }

            inGSystemRepository.save(
                InGSystem(
                    rateOfOpening = rateOfOpening,
                    openSignal = openSignal,
                    site = site,
                    ip = clientIp,
                )
            )
        } catch (e: Exception) {
            throw RuntimeException("InGSystem 데이터 저장 중 에러 발생, message : ${e.message}")
        }

        return "200 OK, save success"
    }
}
