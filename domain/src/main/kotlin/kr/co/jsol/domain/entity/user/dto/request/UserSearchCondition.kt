package kr.co.jsol.domain.entity.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.domain.common.BaseCondition

@Schema
data class UserSearchCondition(
    @Schema(description = "해당 name를 포함하는 값 검색, 빈 값 가능하며 빈 값을 넣으면 필터링하지 않음")
    val name: String?,

    @Schema(description = "해당 siteSeq를 포함하는 값 검색, 빈 값 가능하며 빈 값을 넣으면 필터링하지 않음")
    val siteSeq: Long?,

    @Schema(description = "해당 siteName를 포함하는 값 검색, 빈 값 가능하며 빈 값을 넣으면 필터링하지 않음")
    val siteName: String?,

    @Schema(description = "해당 crop를 포함하는 값 검색, 빈 값 가능하며 빈 값을 넣으면 필터링하지 않음")
    val siteCrop: String?,

    @Schema(description = "해당 location를 포함하는 값 검색, 빈 값 가능하며 빈 값을 넣으면 필터링하지 않음")
    val siteLocation: String?,
) : BaseCondition()
