package kr.co.jsol.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kr.co.jsol.domain.entity.user.UserService
import kr.co.jsol.domain.entity.user.dto.request.UserRequest
import kr.co.jsol.domain.entity.user.dto.request.UserUpdateRequest
import kr.co.jsol.domain.entity.user.dto.response.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
@Schema(description = "관리자 API",
    )
class AdminController(
    private val userService: UserService,
) {

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

    @Operation(summary = "사용자 계정 중복 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @GetMapping("/users/exist/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun isExistUsername(@PathVariable id: String): Boolean {
        return userService.isExistUserById(id)
    }

    @Operation(summary = "사용자 리스트 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다."),
        ApiResponse(responseCode = "403", description = "권한이 부족합니다.")
    )
    @GetMapping("/users")
    @ResponseStatus(value = HttpStatus.OK)
    fun getUserList(): List<UserResponse> {
        return userService.getAll()
    }

    @Operation(summary = "사용자 수정")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.", content = [Content()])
    )
    @PutMapping("/user")
    @ResponseStatus(value = HttpStatus.OK)
    fun putUser(@RequestBody userUpdateRequest: UserUpdateRequest,): Unit {
        userService.updateUser(userUpdateRequest)
    }

    @Operation(summary = "사용자 삭제")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.", content = [Content()])
    )
    @DeleteMapping("/user/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun deleteUser(@PathVariable id: String): Boolean {
        return userService.deleteUserById(id)
    }
}
