package kr.co.jsol.domain.entity.sensor

import kr.co.jsol.common.exception.entities.site.SiteNotFoundException
import kr.co.jsol.domain.entity.sensor.dto.SensorTcpDto
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import org.springframework.stereotype.Service

@Service
class SensorService(
    private val sensorRepository: SensorRepository,
    private val siteRepository: SiteRepository,
) {
    fun saveSensor(sensorDto: SensorTcpDto, clientIp: String): Site {
        val siteSeq = sensorDto.siteSeq

        // siteSeq로 site 정보 가져오기
        val site: Site = siteRepository.findBySeq(siteSeq) ?: throw SiteNotFoundException()

        // site의 ip 정보 업데이트
        val sensor = sensorDto.toEntity(site)
        sensor.create(clientIp)

        sensorRepository.save(sensor)
        if (site.ip != clientIp) {
            site.updateLastSensorIp(ip = clientIp)
            siteRepository.save(site)
        }
        return site
    }
}
