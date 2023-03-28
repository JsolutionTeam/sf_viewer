package kr.co.jsol.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kr.co.jsol.domain.entity.opening.dto.OpeningResDto
import kr.co.jsol.domain.entity.site.GetSiteService
import kr.co.jsol.domain.entity.site.dto.request.SiteSearchCondition
import kr.co.jsol.domain.entity.site.dto.response.RealTimeResponse
import kr.co.jsol.domain.entity.site.dto.response.SiteResponse
import kr.co.jsol.domain.entity.site.dto.response.SummaryResponse
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1")
class SiteController(
    private val siteService: GetSiteService
) {

    private val log = LoggerFactory.getLogger(SiteController::class.java)

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
        val condition = SiteSearchCondition(
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
        val condition = SiteSearchCondition(
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
        val condition = SiteSearchCondition(
            siteSeq = siteSeq,
            null,
            null,
        )
        return siteService.getRealTime(condition)
    }
}
