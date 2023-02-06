package kr.co.jsol.domain.entity.ingsystem.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime
import javax.persistence.*

data class InGSystemDto @QueryProjection constructor(
    var siteSeq: Long,

    // 개폐장치 움직임 정도(rate)
    val rateOfOpening: Double = 0.0,

    // 개폐장치 작동 시그널 값 -1: 역방향, 0: 정지, 1: 정방향
    val openSignal: Int = 0,

    // 정보 수집 시간
    val regTime: LocalDateTime,

    val machineId: Long = 0,
)
