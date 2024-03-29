package kr.co.jsol.domain.entity.sensor

import kr.co.jsol.domain.entity.sensor.dto.SensorTcpDto
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SensorService(
    private val sensorRepository: SensorRepository,
    private val siteRepository: SiteRepository,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun saveSensor(sensorDto: SensorTcpDto, clientIp: String): Site {
        val siteSeq = sensorDto.siteSeq

        // siteSeq로 site 정보 가져오기
        val optional = siteRepository.findById(siteSeq)
        if (optional.isEmpty) {
            throw IllegalArgumentException("존재하지 않는 농장 번호입니다.")
        }
        val site = optional.get()

        // site의 ip 정보 업데이트

        // dto to entity
        val sensor = sensorDto.toEntity(site)
        sensor.create(clientIp)

        sensorRepository.save(sensor)
        if (site.ip != clientIp) {
            log.info("ip 변경 감지 기존 ip: ${site.ip} 변경 ip: $clientIp")
            site.updateLastSensorIp(ip = clientIp)
            siteRepository.save(site)
        }
        return site
    }
}
