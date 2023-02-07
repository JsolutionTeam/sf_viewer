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
import kr.co.jsol.domain.entity.ingsystem.QInGSystem.Companion.inGSystem
import kr.co.jsol.domain.entity.ingsystem.dto.InGSystemDto
import kr.co.jsol.domain.entity.ingsystem.dto.QInGSystemDto
import kr.co.jsol.domain.entity.micro.QMicro.Companion.micro
import kr.co.jsol.domain.entity.micro.dto.MicroDto
import kr.co.jsol.domain.entity.micro.dto.QMicroDto
import kr.co.jsol.domain.entity.site.dto.request.SearchCondition
import kr.co.jsol.domain.entity.site.dto.response.RealTimeResponse
import kr.co.jsol.domain.entity.site.dto.response.SummaryResponse
import kr.co.jsol.domain.entity.util.formatDateTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

@Repository
class SiteQuerydslRepository(
    private val queryFactory: JPAQueryFactory,
) {

    // JPAQueryFactory를 사용하려면 QueryDslConfig 파일에 Bean 등록 해줘야함.

//    fun getSqlByCondition(condition: SearchCondition): JPAQuery<SearchResponse> {
//
//    }

    fun getRealTime(condition: SearchCondition): RealTimeResponse {
        val (siteSeq) = condition

        val response = RealTimeResponse(siteSeq = siteSeq)

        val microDto = queryFactory
            .select(
                Projections.constructor(
                    MicroDto::class.java,
                    Expressions.asNumber(siteSeq).`as`("siteSeq"),
                    micro.regTime,
                    micro.temperature,
                    micro.relativeHumidity,
                    micro.solarRadiation,
                    micro.rainfall,
                    micro.earthTemperature,
                    micro.windDirection,
                    micro.windSpeed,
                )
            )
            .from(micro)
            .where(micro.site.id.eq(siteSeq))
            .orderBy(micro.id.desc())
            .limit(1)
            .fetchOne()

        val co2Dto = queryFactory
            .select(
                Projections.constructor(
                    Co2Dto::class.java,
                    Expressions.asNumber(siteSeq).`as`("siteSeq"),
                    co2Logger.regTime,
                    co2Logger.co2,
                    co2Logger.temperature,
                    co2Logger.relativeHumidity,
                )
            )
            .from(co2Logger)
            .where(co2Logger.site.id.eq(siteSeq))
            .orderBy(co2Logger.id.desc())
            .limit(1)
            .fetchOne()

        val inGDto = queryFactory
            .select(
                Projections.constructor(
                    InGSystemDto::class.java,
                    Expressions.asNumber(siteSeq).`as`("siteSeq"),
                    inGSystem.rateOfOpening,
                    inGSystem.openSignal,
                    inGSystem.regTime,
                    inGSystem.machineId,
                )
            )
            .from(inGSystem)
            .where(inGSystem.site.id.eq(siteSeq))
            .orderBy(inGSystem.id.desc())
            .limit(1)
            .fetchOne()

        if (co2Dto != null) {
            response.setCo2Info(co2Dto)
        }
        if (microDto != null) {
            response.setMicro(microDto)
        }
        if (inGDto != null) {
            response.setInGSystem(inGDto)
        }

        return response
    }

    fun getSummaryBySearchCondition(condition: SearchCondition): List<SummaryResponse> {

        // 하나의 쿼리에서 두 개의 테이블을 조인하고 가져오는것이 굉장히 오래걸림.
        // 그러므로 시간단위로 그룹바이한 값을 두 개를 조회해서 dto에 세팅하는 것으로 변경

        val microTime: DateTemplate<LocalDateTime> =formatDateTemplate(micro.regTime)
        val co2Time: DateTemplate<LocalDateTime> =formatDateTemplate(co2Logger.regTime)

        val siteSeq = condition.siteSeq
        val initDto = initTime(condition)
        val startTime: LocalDateTime = initDto.startTime!!
        val endTime: LocalDateTime = initDto.endTime!!


        val co2: List<Co2Dto> = queryFactory.select(
            QCo2Dto(
                co2Logger.site.id.`as`("siteSeq"),
//                co2Time.`as`("regTime"),
                co2Logger.regTime,
                co2Logger.co2.`as`("co2"),
                co2Logger.temperature.`as`("temperature"),
                co2Logger.relativeHumidity.`as`("relativeHumidity"),
            )
        ).from(co2Logger)
            .where(
                co2Logger.site.id.eq(siteSeq),
                betweenTime(co2Logger.regTime, startTime, endTime)
            )
            .groupBy(co2Time)
            .orderBy(co2Time.desc())
            .fetch()

        val micro: List<MicroDto> = queryFactory.select(
            QMicroDto(
                micro.site.id.`as`("siteSeq"),
//                microTime.`as`("reg_dtm"),
                micro.regTime,
                micro.temperature.`as`("temperature"),
                micro.relativeHumidity.`as`("relativeHumidity"),
                micro.solarRadiation.`as`("solarRadiation"),
                micro.rainfall.`as`("rainfall"),
                micro.earthTemperature.`as`("earthTemperature"),
                micro.windDirection.`as`("windDirection"),
                micro.windSpeed.`as`("windSpeed"),
            )
        ).from(micro)
            .where(
                micro.site.id.eq(siteSeq),
                betweenTime(micro.regTime, startTime, endTime)
            )
            .groupBy(microTime)
            .orderBy(microTime.desc())
            .fetch()
        return SummaryResponse.of(siteSeq, co2, micro)
    }

    fun getDoorSummaryBySearchCondition(condition: SearchCondition): List<InGSystemDto> {
        val inGTime: DateTemplate<LocalDateTime> =formatDateTemplate(inGSystem.regTime, "%Y %m %d %H %i")

        val siteSeq = condition.siteSeq
        val initDto = initTime(condition)
        val startTime: LocalDateTime = initDto.startTime!!
        val endTime: LocalDateTime = initDto.endTime!!

        return queryFactory.select(
            QInGSystemDto(
                inGSystem.site.id,
                inGSystem.rateOfOpening,
                inGSystem.openSignal,
                inGSystem.regTime,
                inGSystem.machineId,
            )
        ).from(inGSystem)
            .where(
                inGSystem.site.id.eq(siteSeq),
                betweenTime(inGSystem.regTime, startTime, endTime)
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
