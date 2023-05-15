package kr.co.jsol.socket.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kr.co.jsol.domain.entity.user.dto.request.UserRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/manage")
@Schema(
    description = "서버 관리 API",
)
class ManagerController {

    @Operation(summary = "서버 작동 확인")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "서버 작동 중"),
    )
    @PostMapping("/health-check")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    fun healthCheck(@RequestBody userRequest: UserRequest): String {
        return "GOOD"
    }
}
