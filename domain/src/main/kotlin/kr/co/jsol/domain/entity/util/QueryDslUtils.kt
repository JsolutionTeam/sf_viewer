package kr.co.jsol.domain.entity.util

import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.Expressions
import java.time.LocalDateTime

fun formatDateTemplate(
    column: DateTimePath<LocalDateTime>,
    format: String = "%Y %m %d %H",
) =
    Expressions.dateTemplate(
        LocalDateTime::class.java,
        "DATE_FORMAT({0}, {1})",
        column,
        format
    )
