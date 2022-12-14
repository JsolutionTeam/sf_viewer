package kr.co.jsol.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import kr.co.jsol.domain.entity.user.User
import kr.co.jsol.domain.entity.user.UserService
import kr.co.jsol.domain.entity.user.dto.request.LoginRequest
import kr.co.jsol.domain.entity.user.dto.request.UserRequest
import kr.co.jsol.domain.entity.user.dto.response.LoginResponse
import kr.co.jsol.domain.entity.user.dto.response.UserResponse
import kr.co.jsol.domain.jwt.dto.RefreshTokenDto
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
) {

    @Operation(summary = "토큰 재발급")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "403", description = "유효하지 않은 토큰입니다.", content = [Content()])
    )
    @GetMapping("refresh")
    @ResponseStatus(value = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority(\"ADMIN\", \"USER\")")
    fun refreshToken(@AuthenticationPrincipal userDetails: User): RefreshTokenDto {
        return userService.refreshToken(userDetails.username)
    }

    @Operation(summary = "로그인")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "403", description = "로그인 실패하셨습니다.", content = [Content()])
    )
    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse {
        return userService.login(loginRequest)
    }

    @Operation(summary = "계정 생성")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "성공"),
        ApiResponse(responseCode = "400", description = "계정 생성에 실패했습니다.", content = [Content()])
    )
    @PostMapping("/user")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createUser(@RequestBody userRequest: UserRequest): UserResponse {
        return userService.createUser(userRequest)
    }
}
