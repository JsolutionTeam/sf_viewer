package kr.co.jsol.domain.entity.rdacolumn

import kr.co.jsol.domain.entity.rdacolumn.dto.response.RdaColumnResponse
import kr.co.jsol.domain.entity.sensor.SensorService
import kr.co.jsol.domain.entity.site.SiteService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RdaColumnService(
    private val radColumnRepository: RdaColumnRepository,
    private val sensorService: SensorService,
    private val siteService: SiteService,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun list(): List<RdaColumnResponse> {
        return radColumnRepository.findAll().map {
            RdaColumnResponse(
                property = it.property,
                key = it.target,
                description = it.description,
                no = it.no,
            )
        }
    }
}

/*
,,1,,2,,3partn_hd_extrl,,4wd,,5arvlty,,6solrad_qy,,7,,,,,,,,11
 */
