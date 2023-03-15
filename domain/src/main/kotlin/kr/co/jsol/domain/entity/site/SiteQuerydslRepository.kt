package kr.co.jsol.domain.entity.site

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.DateTemplate
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.jsol.domain.entity.co2.QCo2Logger.Companion.co2Logger
import kr.co.jsol.domain.entity.co2.dto.Co2Dto
import kr.co.jsol.domain.entity.co2.dto.QCo2Dto
import kr.co.jsol.domain.entity.micro.QMicro.Companion.micro
import kr.co.jsol.domain.entity.micro.dto.MicroDto
import kr.co.jsol.domain.entity.micro.dto.QMicroDto
import kr.co.jsol.domain.entity.opening.QOpening.Companion.opening
import kr.co.jsol.domain.entity.opening.dto.OpeningResDto
import kr.co.jsol.domain.entity.opening.dto.QOpeningResDto
import kr.co.jsol.domain.entity.sensor.QSensor.Companion.sensor
import kr.co.jsol.domain.entity.site.dto.request.SearchCondition
import kr.co.jsol.domain.entity.site.dto.response.RealTimeResponse
import kr.co.jsol.domain.entity.site.dto.response.SummaryResponse
import kr.co.jsol.domain.entity.util.formatDateTemplate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

@Repository
class SiteQuerydslRepository(
    private val queryFactory: JPAQueryFactory,
) {

    // JPAQueryFactory를 사용하려면 QueryDslConfig 파일에 Bean 등록 해줘야함.
    private val log = LoggerFactory.getLogger(SiteQuerydslRepository::class.java)

    fun getRealTime(condition: SearchCondition): RealTimeResponse {
        val (siteSeq) = condition
//
        val response = RealTimeResponse(siteSeq = siteSeq)

        val co2Dto = queryFactory
            .select(qCo2)
            .from(co2Logger)
            .where(co2Logger.site.id.eq(siteSeq))
            .orderBy(co2Logger.id.desc())
            .limit(1)
            .fetchOne()

        val microDto = queryFactory
            .select(qMicro)
            .from(micro)
            .where(micro.site.id.eq(siteSeq))
            .orderBy(micro.id.desc())
            .limit(1)
            .fetchOne()

        val sensorDto = queryFactory
            .select(qSensor)
            .from(sensor)
            .where(sensor.site.id.eq(siteSeq))
            .orderBy(sensor.id.desc())
            .limit(1)
            .fetchOne()

        val openingResDto = queryFactory
            .select(
                Projections.constructor(
                    OpeningResDto::class.java,
                    Expressions.asNumber(siteSeq).`as`("siteSeq"),
                    opening.rateOfOpening,
                    opening.openSignal,
                    opening.regTime,
                    opening.machineId,
                )
            )
            .from(opening)
            .where(opening.site.id.eq(siteSeq))
            .orderBy(opening.id.desc())
            .limit(1)
            .fetchOne()


        // micro는 호보, 인지 데이터 두 개가 있어 최신 데이터 판별하여 대입
        if (sensorDto != null && microDto != null) {
            if(sensorDto.regTime.isAfter(microDto.regTime)) {
                log.info("ing sensor 데이터가 좀 더 최신임")
                response.setMicro(sensorDto)
            } else {
                log.info("hobo sensor 데이터가 좀 더 최신임")
                response.setMicro(microDto)
            }
        }else if(sensorDto != null) {
            log.info("sensor 데이터만 있음")
            response.setMicro(sensorDto)
        }else if(microDto != null) {
            log.info("hobo 데이터만 있음")
            response.setMicro(microDto)
        }

        // 개폐 데이터가 있다면 대입
        if (openingResDto != null) {
            log.info("개폐 데이터가 있음")
            response.setOpening(openingResDto)
        }

        // co2 데이터가 있다면 대입
        if (co2Dto != null) {
            log.info("co2 데이터가 있음")
            response.setCo2(co2Dto)
        }

        return response
    }

    fun getSummaryBySearchCondition(condition: SearchCondition): List<SummaryResponse> {

        // 하나의 쿼리에서 두 개의 테이블을 조인하고 가져오는것이 굉장히 오래걸림.
        // 그러므로 시간단위로 그룹바이한 값을 두 개를 조회해서 dto에 세팅하는 것으로 변경

        val siteSeq = condition.siteSeq
        val initDto = initTime(condition)
        val startTime: LocalDateTime = initDto.startTime!!
        val endTime: LocalDateTime = initDto.endTime!!


        val co2List: List<Co2Dto> = queryFactory
            .select(qCo2)
            .from(co2Logger)
            .where(
                co2Logger.site.id.eq(siteSeq),
                betweenTime(co2Logger.regTime, startTime, endTime)
            )
            .groupBy(co2Logger.regTime)
            .orderBy(co2Logger.regTime.desc())
            .fetch()


        val microDto: List<MicroDto> = queryFactory
            .select(qMicro)
            .from(micro)
            .where(
                micro.site.id.eq(siteSeq),
                betweenTime(micro.regTime, startTime, endTime)
            )
            .groupBy(micro.regTime)
            .orderBy(micro.regTime.desc())
            .fetch()


        val sensor: List<MicroDto> = queryFactory.select(
            qSensor
        ).from(sensor)
            .where(
                sensor.site.id.eq(siteSeq),
                betweenTime(sensor.createdAt, startTime, endTime)
            )
            .groupBy(sensor.createdAt)
            .orderBy(sensor.createdAt.desc())
            .fetch()

        // ing + hobo
        log.info("microDto.size : ${microDto.size}")
        log.info("sensor.size : ${sensor.size}")

        val microList = microDto + sensor
        return SummaryResponse.of(siteSeq, co2List, microList)
    }

    fun getDoorSummaryBySearchCondition(condition: SearchCondition): List<OpeningResDto> {
        val inGTime: DateTemplate<LocalDateTime> = formatDateTemplate(opening.regTime, "%Y %m %d %H %i")

        val siteSeq = condition.siteSeq
        val initDto = initTime(condition)
        val startTime: LocalDateTime = initDto.startTime!!
        val endTime: LocalDateTime = initDto.endTime!!


        return queryFactory.select(qOpening).from(opening)
            .where(
                opening.site.id.eq(siteSeq),
                betweenTime(opening.regTime, startTime, endTime)
            )
            .groupBy(inGTime)
            .orderBy(inGTime.desc())
            .fetch()
    }

    private fun initTime(condition: SearchCondition): SearchCondition {
        var startTime = condition.startTime
        var endTime = condition.endTime

        val startDate = YearMonth.now().atDay(1) // .atDay(1)
        val endDate = YearMonth.now().plusMonths(1).atDay(1)
        val time = LocalTime.of(0, 0, 0)

        // startTime이 null이라면 기본 값 지정.
        if (startTime == null) startTime = LocalDateTime.of(startDate, time)
        if (endTime == null) endTime =
            LocalDateTime.of(endDate, time)

        return SearchCondition(
            condition.siteSeq,
            startTime = startTime,
            endTime = endTime,
        )
    }

    private fun betweenTime(
        value: DateTimePath<LocalDateTime>,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): BooleanExpression? {
        return value.between(startTime, endTime) ?: null
    }
}


private val qMicro = QMicroDto(
    micro.site.id.`as`("siteSeq"),
    micro.regTime,
    micro.temperature.`as`("temperature"),
    micro.relativeHumidity.`as`("relativeHumidity"),
    micro.solarRadiation.`as`("solarRadiation"),
    micro.rainfall.`as`("rainfall"),
    micro.earthTemperature.`as`("earthTemperature"),
    micro.windDirection.`as`("windDirection"),
    micro.windSpeed.`as`("windSpeed"),
)

private val qSensor = QMicroDto(
    sensor.site.id.`as`("siteSeq"),
    sensor.createdAt,
    sensor.temperature.`as`("temperature"),
    sensor.relativeHumidity.`as`("relativeHumidity"),
    sensor.solarRadiation.`as`("solarRadiation"),
    sensor.rainfall.`as`("rainfall"),
    sensor.earthTemperature.`as`("earthTemperature"),
    sensor.windDirection.`as`("windDirection"),
    sensor.windSpeed.`as`("windSpeed"),
)

private val qCo2 = QCo2Dto(
    co2Logger.site.id.`as`("siteSeq"),
    co2Logger.regTime,
    co2Logger.co2.`as`("co2"),
)

private val qOpening = QOpeningResDto(
    opening.site.id,
    opening.rateOfOpening,
    opening.openSignal,
    opening.regTime,
    opening.machineId,
)
