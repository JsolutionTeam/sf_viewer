package kr.co.jsol.rda

import com.querydsl.core.types.dsl.BooleanPath
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.jsol.domain.entity.micro.QMicro.Companion.micro
import kr.co.jsol.domain.entity.sensor.QSensor.Companion.sensor
import kr.co.jsol.domain.entity.site.QSite.Companion.site
import kr.co.jsol.domain.entity.site.SiteRepository
import kr.co.jsol.domain.entity.util.findByIdOrThrow
import kr.co.jsol.rda.ERdaType.CO2
import kr.co.jsol.rda.ERdaType.MICRO
import kr.co.jsol.rda.ERdaType.SENSOR
import kr.co.jsol.rda.dto.QRdaDataDto
import kr.co.jsol.rda.dto.RdaDataDto
import kr.co.jsol.rda.dto.ToSendRDAResponse
import org.springframework.stereotype.Repository

@Repository
class RdaQuery(
    private val queryFactory: JPAQueryFactory,
    private val siteRepository: SiteRepository,
) {
    private val qMicro = QRdaDataDto(
        micro.id.`as`("id"),
        micro.site.id.`as`("siteSeq"),
        micro.regTime.`as`("regTime"),
        micro.temperature.`as`("temperature"),
        micro.relativeHumidity.`as`("relativeHumidity"),
        micro.solarRadiation.`as`("solarRadiation"),
        micro.rainfall.`as`("rainfall"),
        micro.earthTemperature.`as`("earthTemperature"),
        // 0.0 입력
        Expressions.asNumber(0.0).`as`("earthHumidity"),
        micro.windDirection.`as`("windDirection"),
        micro.windSpeed.`as`("windSpeed"),
        Expressions.asNumber(0.0).`as`("cropTemperature"),
        Expressions.asNumber(0.0).`as`("cropHumidity"),
    )

    private val qSensor = QRdaDataDto(
        sensor.id.`as`("id"),
        sensor.site.id.`as`("siteSeq"),
        sensor.createdAt.`as`("regTime"),
        sensor.temperature.`as`("temperature"),
        sensor.humidity.`as`("relativeHumidity"),
        sensor.solarRadiation.`as`("solarRadiation"),
        sensor.rainfall.`as`("rainfall"),
        sensor.earthTemperature.`as`("earthTemperature"),
        sensor.earthHumidity.`as`("earthHumidity"),
        sensor.windDirection.`as`("windDirection"),
        sensor.windSpeed.`as`("windSpeed"),
        sensor.cropTemperature.`as`("cropTemperature"),
        sensor.cropHumidity.`as`("cropHumidity"),
    )

    fun getDataToSendForRDA(siteSeq: Long, type: ERdaType): List<ToSendRDAResponse> {
        val siteEntity = siteRepository.findByIdOrThrow(siteSeq, "해당 사이트가 없습니다.")

        if (type == CO2) throw IllegalArgumentException("지원하지 않는 타입입니다.")

        val select: QRdaDataDto = when (type) {
            MICRO -> qMicro
            SENSOR -> qSensor
            else -> throw IllegalArgumentException("지원하지 않는 타입입니다.")
        }

        val from = when (type) {
            MICRO -> micro
            SENSOR -> sensor
            else -> throw IllegalArgumentException("지원하지 않는 타입입니다.")
        }

        val innerJoinSite = when (type) {
            MICRO -> micro.site
            SENSOR -> sensor.site
            else -> throw IllegalArgumentException("지원하지 않는 타입입니다.")
        }

        val isSend: BooleanPath = when (type) {
            MICRO -> micro.isSend
            SENSOR -> sensor.isSend
            else -> throw IllegalArgumentException("지원하지 않는 타입입니다.")
        }

        val createdAt = when (type) {
            MICRO -> micro.regTime
            SENSOR -> sensor.createdAt
            else -> throw IllegalArgumentException("지원하지 않는 타입입니다.")
        }

        val sensorData: List<RdaDataDto> = queryFactory
            .select(select)

            .from(from)

            .innerJoin(site).on(site.eq(innerJoinSite))

            .where(
                site.id.eq(siteSeq),
                site.apiKey.isNotEmpty,
                isSend.isFalse,
            )

            .groupBy(createdAt)
            .orderBy(createdAt.asc())
            .fetch()

        return ToSendRDAResponse.toList(siteEntity, sensorData)
    }
}
