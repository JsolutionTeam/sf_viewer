package kr.co.jsol.api.controller

import micro.dto.request.SearchCondition
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kr.co.jsol.api.entity.site.SiteService
import kr.co.jsol.api.entity.site.dto.response.SearchResponse
import kr.co.jsol.api.entity.site.dto.response.SiteResponse
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1")
class SiteController(
    private val siteService: SiteService
) {

    @GetMapping("/site/list")
    fun getSiteList(): ResponseEntity<List<SiteResponse>> {
        return ResponseEntity(siteService.getSiteList(), HttpStatus.OK)
    }

    @GetMapping("/site/{siteSeq}/summary")
    fun getSiteSummary(
        @PathVariable(required = true) siteSeq: Long,
        @RequestParam(required = false) startTime: LocalDateTime?,
        @RequestParam(required = false) endTime: LocalDateTime?,
    ): ResponseEntity<List<SearchResponse>> {
        val condition = SearchCondition(
            siteSeq = siteSeq,
            startTime = startTime,
            endTime = endTime,
        )
        println("contition = $condition")
        val summaries: List<SearchResponse> = siteService.getByRegTime(condition)
        println("summaries.size = ${summaries.size}")
        return ResponseEntity(summaries, HttpStatus.OK)
    }

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
