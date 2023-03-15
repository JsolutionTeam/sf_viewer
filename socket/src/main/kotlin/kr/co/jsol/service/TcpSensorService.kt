package kr.co.jsol.service

import kr.co.jsol.domain.entity.opening.OpeningService
import kr.co.jsol.domain.entity.sensor.SensorService
import kr.co.jsol.domain.entity.sensor.dto.SensorTcpDto
import kr.co.jsol.domain.entity.site.SiteService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TcpSensorService(
    private val openingService: OpeningService,
    private val sensorService: SensorService,
    private val siteService: SiteService,
) {
    val log = LoggerFactory.getLogger(TcpSensorService::class.java)

    @Value("\${ingsystem.message.delimiter:,}")
    private val messageDelimiter: String = ","

    @Value("\${ingsystem.message.default-delay:60}")
    private val defaultDelay: Long = 60L

    fun getSiteDelayByIp(ip: String): Long {
        return siteService.getDelayByIp(ip)
    }

    fun handleTcpMessage(message: String, clientIp: String): Long {
        // serial deserial을 거쳐서 나온 message를 처리하는 곳
        val payload = message.trim()
        // 빈 값이 넘어오면 처리하지 않음
        if (payload.isNullOrBlank()) {
            throw RuntimeException("payload is null")
        }

        if (!isValidMessage(payload)) {
            throw RuntimeException("Invalid message")
        }

        /*
            001 -> 센서장치
                1. 농가번호
                2. 장비번호
                3. 기상대 강우량
                4. 기상대 풍속
                5. 기상대 풍향
                6. 기상대 온도
                7. 기상대 습도
                8. 기상대 지온
                9. 기상대 일사량
                10. 기상대 지습
                11. 내부 카운터( 데이터 보낼때마다 카운트 증가, 0이면 장비가 리셋 됨을 의미함 )
            002 -> 개폐장치
         */
        val split = payload.split(messageDelimiter)
        log.info("split : $split, size: ${split.size}")

        // 이 부분부터 분기가 시작되어야 함.
        // 고유번호에 따라 개폐장치 처리 혹은 센서장치 처리
        val processKey = split[1]
        when (processKey) {
            // 001 => 센서장치
            "001" -> {
                log.info("001, 센서장치, 처리 시작, ip: $clientIp")
                if (!isValidSensorData(split)) {
                    throw RuntimeException("Invalid sensor data")
                }
                val sensorDto: SensorTcpDto = SensorTcpDto().parseSplitMessageToSensorTcpDto(split)
                log.info("sensorDto : $sensorDto")
                return sensorService.saveSensor(sensorDto, clientIp)
            }
            // 002 => 개폐장치
            "002" -> {
                log.info("002, 개폐장치, 처리 시작, ip: $clientIp")
                //        val rateOfOpeningMessage = split[1]
//        val openSignalMessage = split[2]
//
//        val siteSeq: Long = parseSiteSeq(siteSeqMessage)
//        val rateOfOpening: Double = parseRateOfOpening(rateOfOpeningMessage)
//        val openSignal: Int = parseOpenSignal(openSignalMessage)
//
//        openingService.saveInGSystem(siteSeq, rateOfOpening, openSignal, clientIp)
                return defaultDelay
            }
            // 그 외에는 처리하지 않음
            else -> {
                log.info("InGSystem 데이터 추가 완료")
                return defaultDelay
            }
        }
    }

    private fun isValidSensorData(split: List<String>): Boolean {
        // 총 데이터 개수가 11개여야 함.
        if (split.size != 11) {
            log.error("데이터 개수가 11개가 아닙니다.")
            return false
        }

        return true
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
//        if (!isDelimiterCountEq(str, countOfComma)) {
//            log.error("'$messageDelimiter'로 $countOfComma 개로 나누어져야 합니다.")
//            return false
//        }

        return true
    }

    private fun isIotMessage(str: String) = str.startsWith("*") && str.endsWith("#")

    private fun isContainsDelimiter(str: String) = str.contains(",")

    private fun parseRateOfOpening(rateOfOpeningMessage: String): Double {
        // 숫자 외 제거
        val rateOfOpeningStr = rateOfOpeningMessage.replace(Regex("[^0-9]"), "")

        // 1.5 곱하기
        return rateOfOpeningStr.toDouble() * 1.5
    }
}
