package kr.co.jsol.service

import kr.co.jsol.domain.entity.ingsystem.InGSystemService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class TcpInGSystemService(
    private val inGSystemService: InGSystemService,
) {
    val log = LoggerFactory.getLogger(TcpInGSystemService::class.java)

    @Value("\${ingsystem.message.delimiter:,}")
    private val messageDelimiter: String = ","

    @Value("\${ingsystem.message.delimiter-count}")
    private val countOfComma: Int = 0

    fun handleTcpMessage(message: String, clientIp: String? = null) {
        // serial deserial을 거쳐서 나온 message를 처리하는 곳
        val payload = message.trim()
        // 빈 값이 넘어오면 처리하지 않음
        if (payload.isNullOrBlank()) {
            throw RuntimeException("payload is null")
        }

        if (!isValidMessage(payload)) {
            throw RuntimeException("Invalid message")
        }

        val split = payload.split(messageDelimiter)
        val siteSeqMessage = split[0]
        val rateOfOpeningMessage = split[1]
        val openSignalMessage = split[2]

        val siteSeq: Long = parseSiteSeq(siteSeqMessage)
        val rateOfOpening: Double = parseRateOfOpening(rateOfOpeningMessage)
        val openSignal: Int = parseOpenSignal(openSignalMessage)

        inGSystemService.saveInGSystem(siteSeq, rateOfOpening, openSignal, clientIp)

        log.info("InGSystem 데이터 추가 완료")
    }

    private fun isValidMessage(str: String): Boolean {
        log.info("isValidMessage - str : $str")

        // '*'으로 시작하고 '#'으로 끝나는가?
        if (!isIotMessage(str)) {
            log.error("*으로 시작하고 #으로 끝나야 합니다.")
            return false
        }

        // ','가 포함되어 있는가?
        if (!isContainsDelimiter(str)) {
            log.error("'$messageDelimiter'가 포함되어 있어야 합니다.")
            return false
        }

        // 구분자가 ${countOfComma} 개로 나뉘어 지는가?
        if (!isDelimiterCountEq(str, countOfComma)) {
            log.error("'$messageDelimiter'로 $countOfComma 개로 나누어져야 합니다.")
            return false
        }

        return true
    }

    private fun isIotMessage(str: String) = str.startsWith("*") && str.endsWith("#")

    private fun isContainsDelimiter(str: String) = str.contains(",")

    private fun isDelimiterCountEq(str: String, count: Int) = str.split(",").size - 1 == count

    private fun parseSiteSeq(siteSeqMessage: String): Long {
        val siteSeqStr = siteSeqMessage.replace(Regex("[^-0-9]"), "")
        return siteSeqStr.toLong()
    }

    private fun parseRateOfOpening(rateOfOpeningMessage: String): Double {
        // 숫자 외 제거
        val rateOfOpeningStr = rateOfOpeningMessage.replace(Regex("[^0-9]"), "")

        // 1.5 곱하기
        return rateOfOpeningStr.toDouble() * 1.5
    }

    private fun parseOpenSignal(openSignalMessage: String): Int {
        val openSignalStr = openSignalMessage.replace(Regex("[^-0-9]"), "")

        return openSignalStr.toInt()
    }
}
