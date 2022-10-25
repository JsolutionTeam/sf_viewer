package kr.co.jsol.domain.entity.site

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.DateTemplate
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.jsol.domain.entity.co2.QCo2Logger.co2Logger
import kr.co.jsol.domain.entity.micro.QMicro.micro
import kr.co.jsol.domain.entity.site.QSite.site
import kr.co.jsol.domain.entity.site.dto.response.SearchResponse
import kr.co.jsol.domain.entity.site.dto.request.SearchCondition
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

@Component
class SiteQuerydslRepository(
    private val queryFactory: JPAQueryFactory,
) {

    // JPAQueryFactory를 사용하려면 QueryDslConfig 파일에 Bean 등록 해줘야함.

//    fun getSqlByCondition(condition: SearchCondition): JPAQuery<SearchResponse> {
//
//    }

    fun getRealTime(condition: SearchCondition): List<SearchResponse> {
        val (siteSeq) = condition
        val startTime = LocalDateTime.now().withMinute(0).withSecond(0)
        val endTime = startTime.plusHours(1)

        return queryFactory
            .select(
                Projections.constructor(
                    SearchResponse::class.java,
                    co2Logger.co2,
                    micro.temperature,
                    micro.relativeHumidity,
                    micro.solarRadiation,
                    micro.rainfall,
                    micro.earthTemperature,
                    micro.windDirection,
                    micro.windSpeed,
                    micro.regTime,
                    co2Logger.regTime,
                )
            )
            .from(site)
            .leftJoin(co2Logger)
            .on(
                co2Logger.site.eq(site)
                    .and(
                        betweenTime(co2Logger.regTime, startTime!!, endTime!!)
                    )
            )
            .fetchJoin()

            .leftJoin(micro)
            .on(
                micro.site.eq(site)
                    .and(
                        betweenTime(micro.regTime, startTime, endTime)
                    )
            )
            .where(site.id.eq(siteSeq))
            .fetchJoin().limit(1).fetch()
    }

    fun getMicroList(condition: SearchCondition): List<SearchResponse> {

        /**
         select
         avg(co2logger1_.co2)           as col_0_0_,
         avg(micro2_.temperature)       as col_1_0_,
         avg(micro2_.relative_humidity) as col_2_0_,
         avg(micro2_.solar_radiation)   as col_3_0_,
         avg(micro2_.rainfall)          as col_4_0_,
         avg(micro2_.earth_temperature) as col_5_0_,
         avg(micro2_.wind_direction)    as col_6_0_,
         avg(micro2_.wind_speed)        as col_7_0_,
         micro2_.reg_dtm           as mtime,
         co2logger1_.reg_dtm       as ctime
         from tb_site site0_
         left outer join tb_co2_logger co2logger1_ on (co2logger1_.site_seq = site0_.site_seq and
         (co2logger1_.reg_dtm between '2022-09-23T00:00:00.000+0900' and '2022-10-01T00:00:00.000+0900'))
         left outer join tb_micro_station micro2_ on (micro2_.site_seq = site0_.site_seq and
         (micro2_.reg_dtm between '2022-09-23T00:00:00.000+0900' and '2022-10-01T00:00:00.000+0900'))
         where site0_.site_seq=13
         group by
         date_format(micro2_.reg_dtm, '%Y %c %d %H')
         ,date_format(co2logger1_.reg_dtm, '%Y %c %d %H');
         */
        val microTime: DateTemplate<*> =
            Expressions.dateTemplate(
                LocalDateTime::class.java,
                "DATE_FORMAT({0}, {1})",
                micro.regTime,
                "%Y %c %d %H"
            )

        val co2Time: DateTemplate<*> =
            Expressions.dateTemplate(
                LocalDateTime::class.java,
                "DATE_FORMAT({0}, {1})",
                co2Logger.regTime,
                "%Y %c %d %H"
            )

        val (siteSeq, startTime, endTime) = checkTime(condition)
        return queryFactory
            .select(
                Projections.constructor(
                    SearchResponse::class.java,
                    co2Logger.co2.avg(),
                    micro.temperature.avg(),
                    micro.relativeHumidity.avg(),
                    micro.solarRadiation.avg(),
                    micro.rainfall.avg(),
                    micro.earthTemperature.avg(),
                    micro.windDirection.avg(),
                    micro.windSpeed.avg(),
                    micro.regTime,
                    co2Logger.regTime,
                )
            )
            .from(site)
            .leftJoin(co2Logger)
            .on(
                co2Logger.site.eq(site)
                    .and(
                        betweenTime(co2Logger.regTime, startTime!!, endTime!!)
                    )
            )
            .fetchJoin()

            .leftJoin(micro)
            .on(
                micro.site.eq(site)
                    .and(
                        betweenTime(micro.regTime, startTime, endTime)
                    )
            )
            .where(site.id.eq(siteSeq))
            .fetchJoin()
            .groupBy(microTime, co2Time)
            .fetch()
    }

    private fun checkTime(condition: SearchCondition): SearchCondition {
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
