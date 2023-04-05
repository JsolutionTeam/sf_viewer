package kr.co.jsol.domain.entity.sensorDevice

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.jsol.domain.entity.sensorDevice.QSensorDevice.Companion.sensorDevice
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceSearchCondition
import kr.co.jsol.domain.entity.sensorDevice.dto.response.QSensorDeviceResponse
import kr.co.jsol.domain.entity.sensorDevice.dto.response.SensorDeviceResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class SensorDeviceCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : SensorDeviceCustomRepository {

    // JPAQueryFactory를 사용하려면 QueryDslConfig 파일에 Bean 등록 해줘야함.
    private val log = LoggerFactory.getLogger(SensorDeviceCustomRepositoryImpl::class.java)

    override fun getSensorDeviceList(siteSearchCondition: SensorDeviceSearchCondition?): List<SensorDeviceResponse> {
        return queryFactory
            .select(
                QSensorDeviceResponse(
                    sensorDeviceId = sensorDevice.id,
                    type = sensorDevice.type,
                    modelName = sensorDevice.modelName,
                    serialNumber = sensorDevice.serialNumber,
                    ip = sensorDevice.ip,
                    memo = sensorDevice.memo,
                    siteName = sensorDevice.site.name,
                )
            )
            .from(sensorDevice)
            .where(
                search(siteSearchCondition),
            )
            .fetch()
    }


    ///////////////// [ 조건식 ] /////////////////

    private fun search(condition: SensorDeviceSearchCondition?): BooleanBuilder {
        val builder = BooleanBuilder()
        if(condition == null) return builder
        condition.modelName?.let { builder.and(modelNameContains(it)) }
        condition.serialNumber?.let { builder.and(serialNumberContains(it)) }
        condition.ip?.let { builder.and(sensorDevice.ip.contains(it)) }
        condition.memo?.let { builder.and(sensorDevice.memo.contains(it)) }

        // start과 endTime이 모두 null이 아닐 때만 조건식 추가
        if (condition.startTime != null && condition.endTime != null) {
            builder.and(betweenTime(sensorDevice.createdAt, condition.startTime!!, condition.endTime))
        }

        return builder
    }

    private fun modelNameContains(modelName: String): BooleanExpression? {
        return sensorDevice.modelName.contains(modelName) ?: null
    }

    private fun serialNumberContains(serialNumber: String): BooleanExpression? {
        return sensorDevice.serialNumber.contains(serialNumber) ?: null
    }

    private fun betweenTime(
        value: DateTimePath<LocalDateTime>,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): BooleanExpression? {
        return value.between(startTime, endTime) ?: null
    }
}
