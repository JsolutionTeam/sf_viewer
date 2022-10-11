package kr.co.jsol.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import micro.dto.request.SearchCondition
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kr.co.jsol.api.entity.site.SiteService
import kr.co.jsol.api.entity.site.dto.response.SearchResponse
import kr.co.jsol.api.entity.site.dto.response.SiteResponse
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1")
class SiteController(
    private val siteService: SiteService
) {


    @Operation(summary = "농장 전체 정보 조회" )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @GetMapping("/site/list")
    @ResponseStatus(value = HttpStatus.OK)
    fun getSiteList(): List<SiteResponse> {
        return siteService.getSiteList()
    }

    @Operation(summary = "농장 별 기간 수집 데이터 조회", description = "startTime은 기본 값이 금월 1일 endTime은 금월 마지막 일" )
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
    ): List<SearchResponse> {
        val condition = SearchCondition(
            siteSeq = siteSeq,
            startTime = startTime,
            endTime = endTime,
        )
        println("contition = $condition")
        val summaries: List<SearchResponse> = siteService.getByRegTime(condition)
        println("summaries.size = ${summaries.size}")
        return summaries
    }

    @Operation(summary = "농장 별 실시간 수집 데이터 조회" )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @GetMapping("/site/{siteSeq}/realtime")
    fun getSiteSummary(
        @PathVariable(required = true) siteSeq: Long,
    ): ResponseEntity<List<SearchResponse>> {
        val condition = SearchCondition(
            siteSeq = siteSeq,
            null,
            null,
        )
        val realtime: List<SearchResponse> = siteService.getRealTime(condition)
        println("realtime.size = ${realtime.size}")
        return ResponseEntity(realtime, HttpStatus.OK)
    }
}
