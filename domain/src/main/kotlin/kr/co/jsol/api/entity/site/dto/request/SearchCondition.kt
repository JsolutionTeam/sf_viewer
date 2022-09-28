package micro.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(name = "농장 별 수집 데이터 조회 조건 DTO")
data class SearchCondition(
    @Schema(name = "농장 번호", required = true)
    @NotBlank(message = "농장 번호는 필수 입력입니다.")
    @NotNull(message = "농장 번호는 null일 수 없습니다.")
    val siteSeq: Long,

    @Schema(name = "조회 시작 시간", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val startTime: LocalDateTime?,

    @Schema(name = "조회 끝 시간", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val endTime: LocalDateTime?,
)
