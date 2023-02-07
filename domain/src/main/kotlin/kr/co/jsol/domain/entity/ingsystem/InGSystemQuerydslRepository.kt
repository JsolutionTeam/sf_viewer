package kr.co.jsol.domain.entity.ingsystem

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.jsol.domain.entity.ingsystem.QInGSystem.Companion.inGSystem
import kr.co.jsol.domain.entity.ingsystem.dto.InGSystemDto
import kr.co.jsol.domain.entity.ingsystem.dto.QInGSystemDto
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class InGSystemQuerydslRepository(
    private val queryFactory: JPAQueryFactory,
) {

    fun findInGSystemBySiteSeq(siteSeq: Long): InGSystemDto? {
        return queryFactory
            .select(
                QInGSystemDto(
                    inGSystem.site.id,
                    inGSystem.rateOfOpening,
                    inGSystem.openSignal,
                    inGSystem.regTime,
                    inGSystem.machineId,
                )
            )
            .from(inGSystem)
            .where(
                inGSystem.site.id.eq(siteSeq)
            )
            .orderBy(inGSystem.id.desc())
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
