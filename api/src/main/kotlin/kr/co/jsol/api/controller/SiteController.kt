package kr.co.jsol.api.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class SiteController {

    @GetMapping("/site/{siteSeq}/summary")
    fun getSiteSummary(@PathVariable(required = true, name = "농장 번호") siteSeq: Long): ResponseEntity<Any>{
        println("siteSeq = ${siteSeq}")
        return ResponseEntity(siteSeq, HttpStatus.OK)
    }
}
