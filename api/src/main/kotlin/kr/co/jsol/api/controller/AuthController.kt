package kr.co.jsol.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kr.co.jsol.common.jwt.dto.JwtToken
import kr.co.jsol.common.jwt.dto.RefreshTokenDto
import kr.co.jsol.common.jwt.dto.RefreshTokenRequest
import kr.co.jsol.domain.entity.user.AuthService
import kr.co.jsol.domain.entity.user.User
import kr.co.jsol.domain.entity.user.dto.request.LoginRequest
import kr.co.jsol.domain.entity.user.dto.response.LoginResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(
            responseCode = "403",
            description = "유효하지 않은 토큰입니다.",
            content = [Content()]
        )
    )
    @Operation(
        summary = "토큰 재발급 주의, Authorization Bearer토큰은 보내지 않아야 함.",
    )
    @PostMapping("/refresh")
    @ResponseStatus(value = HttpStatus.OK)
    fun refreshToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): JwtToken {
        return authService.refreshToken(refreshTokenRequest)
    }

    @Operation(summary = "로그인")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "403", description = "로그인 실패하셨습니다.", content = [Content()])
    )
    @PostMapping("/login")
    @ResponseStatus(value = HttpStatus.OK)
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse {
        return authService.login(loginRequest)
    }
}
