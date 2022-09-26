package site

import co2.QCo2Logger
import co2.QCo2Logger.co2Logger
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import micro.QMicro.micro
import micro.dto.request.SearchCondition
import org.springframework.stereotype.Component
import site.QSite.site
import site.dto.response.SearchResponse
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

@Component
class SiteQuerydslRepository(
    private val queryFactory: JPAQueryFactory,
) {

    final val sql: JPAQuery<SearchResponse> = queryFactory
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
            )
        )
        .from(micro)
        .leftJoin(site).on(micro.site.eq(site)).fetchJoin()


    val getRealTime: List<SearchResponse> = sql.limit(1).fetch();

    fun getMicroList(condition: SearchCondition): List<SearchResponse> {
        return sql
            .where(
                isBetweenTime(condition),
            )
            .fetch()
    }

    private fun isBetweenTime(condition: SearchCondition): BooleanExpression {
        var startTime = condition.startTime
        var endTime = condition.endTime

        val startDate = YearMonth.now().minusMonths(-1).atDay(1)
        val endDate = YearMonth.now().atDay(1)
        val time = LocalTime.of(0, 0, 0)
        if (startTime == null) startTime = LocalDateTime.of(startDate, time)
        if (endTime == null) endTime = LocalDateTime.of(endDate, time)
        return micro.regTime.between(startTime, endTime)
    }
}
