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

    private fun isValidMessage(str: String): Boolean {
        if (isIotMessage(str)) return false // '*'으로 시작하고 '#'으로 끝나는가?
        if (isContainsComma(str)) return false // ','가 포함되어 있는가?
        return str.split(",").size == 2 // , 구분자로 2개로 나뉘어 지는가?
    }

    private fun isIotMessage(str: String) = str.startsWith("*") && str.endsWith("#")

    private fun isContainsComma(str: String) = str.contains(",")

    private fun parsePulse(pulseStr: String): Double {
        // 숫자 외 제거
        val pulseNumberStr = pulseStr.replace(Regex("[^0-9]"), "")
        println("pulseMessage = $pulseNumberStr")

        // 1.5 곱하기
        return pulseNumberStr.toDouble() * 1.5
    }

    private fun parseDirection(directionStr: String): Int {
        val directionNumberStr = directionStr.replace(Regex("[^-0-9]"), "")
        println("directionMessage = $directionNumberStr")

        return directionNumberStr.toInt()
    }
}
