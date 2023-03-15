package kr.co.jsol.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kr.co.jsol.domain.entity.opening.dto.OpeningResDto
import kr.co.jsol.domain.entity.site.dto.request.SearchCondition
import org.springframework.http.HttpStatus
import kr.co.jsol.domain.entity.site.SiteService
import kr.co.jsol.domain.entity.site.dto.request.SiteCreateRequest
import kr.co.jsol.domain.entity.site.dto.request.SiteUpdateRequest
import kr.co.jsol.domain.entity.site.dto.response.RealTimeResponse
import kr.co.jsol.domain.entity.site.dto.response.SummaryResponse
import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1")
class SiteController(
    private val siteService: SiteService
) {

    private val log = LoggerFactory.getLogger(SiteController::class.java)

    @Operation(summary = "농장 정보 등록")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "등록 성공"),
        ApiResponse(responseCode = "400", description = "요청 데이터 확인필요"),
    )
    @PostMapping("/site")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun saveSite(@RequestBody siteCreateRequest: SiteCreateRequest): Long {
        return siteService.saveSite(siteCreateRequest)
    }

    @Operation(summary = "농장 번호 중복 확인")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @GetMapping("/site/exist/{siteSeq}")
    @ResponseStatus(value = HttpStatus.OK)
    fun existSite(@PathVariable siteSeq: Long): Boolean {
        return siteService.isExistSiteSeq(siteSeq)
    }

    @Operation(summary = "농장 전체 정보 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @GetMapping("/site/list")
    @ResponseStatus(value = HttpStatus.OK)
    fun getSiteList(): List<SiteResponse> {
        return siteService.getSiteList()
    }

    @Operation(summary = "농장 별 기간 수집 데이터(개폐장치 외) 조회", description = "startTime은 기본 값이 금월 1일 endTime은 금월 마지막 일")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @GetMapping("/site/{siteSeq}/summary")
    @ResponseStatus(value = HttpStatus.OK)
    fun getSiteSummary(
        @PathVariable(required = true) siteSeq: Long,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @RequestParam(required = false) startTime: LocalDateTime?,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @RequestParam(required = false) endTime: LocalDateTime?,
    ): List<SummaryResponse> {
        val condition = SearchCondition(
            siteSeq = siteSeq,
            startTime = startTime,
            endTime = endTime,
        )
        log.info("contition = $condition")
        val summaries: List<SummaryResponse> = siteService.getSummaryBySearchCondition(condition)
        log.info("summaries.size = ${summaries.size}")
        return summaries
    }

    @Operation(summary = "농장 별 기간 수집 데이터(개폐장치만) 조회", description = "startTime은 기본 값이 금월 1일 endTime은 금월 마지막 일")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @GetMapping("/site/{siteSeq}/summary/door")
    @ResponseStatus(value = HttpStatus.OK)
    fun getSiteSummaryOnlyOpeningData(
        @PathVariable(required = true) siteSeq: Long,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @RequestParam(required = false) startTime: LocalDateTime?,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @RequestParam(required = false) endTime: LocalDateTime?,
    ): List<OpeningResDto> {
        val condition = SearchCondition(
            siteSeq = siteSeq,
            startTime = startTime,
            endTime = endTime,
        )
        val summaries: List<OpeningResDto> = siteService.getDoorSummaryBySearchCondition(condition)
        log.info("summaries.size = ${summaries.size}")
        return summaries
    }

    @Operation(summary = "농장 별 실시간 수집 데이터 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/site/{siteSeq}/realtime")
    fun getSiteSummary(
        @PathVariable(required = true) siteSeq: Long,
    ): RealTimeResponse {
        val condition = SearchCondition(
            siteSeq = siteSeq,
            null,
            null,
        )
        val realtime =
        return siteService.getRealTime(condition)
    }

    @Operation(summary = "농장 정보 수정")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "등록 성공"),
        ApiResponse(responseCode = "400", description = "요청 데이터 확인필요"),
    )
    @PutMapping("/site/{siteSeq}")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateSite(
        @PathVariable(required = true) siteSeq: Long,
        @RequestBody siteUpdateRequest: SiteUpdateRequest,
    ): Long {
        return siteService.updateSite(siteSeq, siteUpdateRequest)
    }

    @Operation(summary = "농장 정보 삭제")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "등록 성공"),
        ApiResponse(responseCode = "400", description = "요청 데이터 확인필요"),
    )
    @DeleteMapping("/site/{siteSeq}")
    @ResponseStatus(value = HttpStatus.OK)
    fun deleteSite(
        @PathVariable(required = true) siteSeq: Long,
    ): Unit {
        return siteService.deleteSite(siteSeq)
    }
}
