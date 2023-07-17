package kr.co.jsol.domain.entity.site.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.domain.common.BaseCondition
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(description = "농장 별 수집 데이터 조회 조건 DTO")
data class SiteSearchCondition(
    @Schema(description = "농장 번호", required = true)
    @NotBlank(message = "농장 번호는 필수 입력입니다.")
    @NotNull(message = "농장 번호는 null일 수 없습니다.")
    val siteSeq: Long,
) : BaseCondition() {
    constructor(
        siteSeq: Long,
        startTime: LocalDateTime? = null,
        endTime: LocalDateTime? = null,
    ) : this(
        siteSeq = siteSeq,
    ) {
        this.startTime = startTime
        this.endTime = endTime
    }
}
