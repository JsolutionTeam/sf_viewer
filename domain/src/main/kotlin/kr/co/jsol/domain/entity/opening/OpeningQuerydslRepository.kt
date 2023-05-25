package kr.co.jsol.domain.entity.opening

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.jsol.domain.entity.opening.QOpening.Companion.opening
import kr.co.jsol.domain.entity.opening.dto.OpeningResDto
import kr.co.jsol.domain.entity.opening.dto.QOpeningResDto
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class OpeningQuerydslRepository(
    private val queryFactory: JPAQueryFactory,
) {

    fun findInGSystemBySiteSeq(siteSeq: Long): OpeningResDto? {
        return queryFactory
            .select(
                QOpeningResDto(
                    opening.site.id,
                    opening.rateOfOpening,
                    opening.openSignal,
                    opening.regTime,
                    opening.machineId,
                )
            )
            .from(opening)
            .where(
                opening.site.id.eq(siteSeq)
            )
            .orderBy(opening.regTime.desc())
            .limit(1)
            .fetchOne()
    }

    private fun betweenTime(
        value: DateTimePath<LocalDateTime>,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): BooleanExpression? {
        return value.between(startTime, endTime) ?: null
    }
}
