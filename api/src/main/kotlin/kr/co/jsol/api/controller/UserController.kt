package kr.co.jsol.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import kr.co.jsol.domain.entity.user.UserService
import kr.co.jsol.domain.entity.user.dto.response.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class UserController(
    private val userService: UserService,
) {

    @Operation(summary = "사용자 리스트 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "403", description = "유효하지 않은 토큰입니다.", content = [Content()])
    )
    @GetMapping("/users")
    @ResponseStatus(value = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole(\"ADMIN\")")
    fun getUserList(): List<UserResponse> {
        return userService.getAll()
    }
}
