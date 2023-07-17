package kr.co.jsol.domain.entity.user

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.jsol.domain.entity.sensorDevice.QSensorDevice
import kr.co.jsol.domain.entity.site.QSite.Companion.site
import kr.co.jsol.domain.entity.site.dto.request.SiteSearchCondition
import kr.co.jsol.domain.entity.user.QUser.Companion.user
import kr.co.jsol.domain.entity.user.dto.request.UserSearchCondition
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

@Repository
class UserQuerydslRepository(
    private val queryFactory: JPAQueryFactory,
) {

    // JPAQueryFactory를 사용하려면 QueryDslConfig 파일에 Bean 등록 해줘야함.

//    fun getSqlByCondition(condition: SearchCondition): JPAQuery<SearchResponse> {
//
//    }

    fun getUsers(userSearchCondition: UserSearchCondition): List<User> {
        val query = queryFactory
            .selectFrom(user)
            .from(user)
            .leftJoin(user.site, site)
            .orderBy(user.id.asc())

        val builder = BooleanBuilder()

        userSearchCondition.name?.let { builder.and(user.name.contains(it)) }
        userSearchCondition.siteSeq?.let { builder.and(user.site.seq.eq(it)) }
        userSearchCondition.siteCrop?.let { builder.and(user.site.crop.contains(it)) }
        userSearchCondition.siteLocation?.let { builder.and(user.site.location.contains(it)) }
        userSearchCondition.siteName?.let { builder.and(user.site.name.contains(it)) }

        if (userSearchCondition.isDateRangeValid) {
            builder.and(betweenTime(QSensorDevice.sensorDevice.createdAt, userSearchCondition.startDateTime, userSearchCondition.endDateTime))
        }

        return query
            .where(builder)
            .orderBy(user.id.desc())
            .fetch()
    }

    private fun checkTime(condition: SiteSearchCondition): SiteSearchCondition {
        var startTime = condition.startTime
        var endTime = condition.endTime

        val startDate = YearMonth.now().atDay(1) // .atDay(1)
        val endDate = YearMonth.now().plusMonths(1).atDay(1)
        val time = LocalTime.of(0, 0, 0)

        // startTime이 null이라면 기본 값 지정.
        if (startTime == null) startTime = LocalDateTime.of(startDate, time)
        if (endTime == null) endTime =
            LocalDateTime.of(endDate, time)

        return SiteSearchCondition(
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
