package kr.co.jsol.domain.entity.rdacolumn

import kr.co.jsol.common.exception.ConflictException
import kr.co.jsol.domain.entity.rdacolumn.dto.request.RdaColumnCreateRequest
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

    fun save(
        rdaColumnCreateRequest: RdaColumnCreateRequest,
    ): RdaColumn {
        val (eng, kor) = rdaColumnCreateRequest
        var rdaColumnOptional = radColumnRepository.findById(eng)
        if (rdaColumnOptional.isPresent) {
            throw ConflictException()
        }

        return radColumnRepository.save(
            RdaColumn(
                eng = eng,
                kor = kor,
            )
        )
    }

    fun list(): List<RdaColumnResponse> {
        return radColumnRepository.findAll().map {
            RdaColumnResponse(
                eng = it.eng,
                kor = it.kor,
            )
        }
    }

//    fun listWithData(): List<RdaColumnWithDataResponse>{
//
//    }
}
