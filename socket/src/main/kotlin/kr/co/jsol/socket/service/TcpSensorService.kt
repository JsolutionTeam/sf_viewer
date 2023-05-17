package kr.co.jsol.socket.service

import kr.co.jsol.domain.entity.opening.OpeningService
import kr.co.jsol.domain.entity.sensor.SensorService
import kr.co.jsol.domain.entity.sensor.dto.SensorTcpDto
import kr.co.jsol.domain.entity.site.GetSiteService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TcpSensorService(
    private val openingService: OpeningService,
    private val sensorService: SensorService,
    private val siteService: GetSiteService,
) {
    val log = LoggerFactory.getLogger(TcpSensorService::class.java)

    @Value("\${ingsystem.message.delimiter:,}")
    private val messageDelimiter: String = ","

    @Value("\${ingsystem.message.default-delay:60}")
    private val defaultDelay: Long = 60L

    @Value("\${sensors}")
    private val sensors: List<String> = listOf()

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
            번호  키           예시값
            01. 농가번호        001
            02. 고유코드        (001->센서, 002->개폐장치) 001
            03. 수집시간 (yyyyMMddHHmmss) 20230509124600

            04. 강우           0.00
            05. 풍속           3.00
            06. 풍향           1.25
            07. 대기온도        90.00
            08. 대기습도        21.00
            09. 태양광량        20.00
            10. 작물근접온도     19.00
            11. 작물대기습도     20.00
            12. 대지온도        13.00
            13. 대지수분함수율    30.00
        */
        val split = payload.split(messageDelimiter).map {
            it.trim() // 의도치않은 공백 제거
        }
        log.info("split : $split, size: ${split.size}")

        // 이 부분부터 분기가 시작되어야 함.
        // 고유번호에 따라 개폐장치 처리 혹은 센서장치 처리
        when (split[1]) {
            // 001 => 센서장치
            "001" -> {
                log.info("001, 센서장치, 처리 시작, ip: $clientIp")
                if (!isValidSensorData(split)) {
                    throw RuntimeException("Invalid sensor data")
                }
                val sensorDto = SensorTcpDto(split)
                return sensorService.saveSensor(sensorDto, clientIp)
            }
            // 002 => 개폐장치
            "002" -> {
                log.info("002, 개폐장치, 처리 시작, ip: $clientIp")
                val siteSeq = split[0].toLong()
                val rateOfOpening = split[3].toDouble()
                val openSignal = split[4].toInt()
                openingService.saveInGSystem(siteSeq, rateOfOpening, openSignal, clientIp)
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
        log.info("데이터 개수 : ${sensors.size}개")
        return split.size == sensors.size
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
