package kr.co.jsol.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.jsol.domain.entity.site.SiteService
import kr.co.jsol.domain.entity.user.UserService
import kr.co.jsol.domain.entity.user.dto.request.UserRequest
import kr.co.jsol.domain.entity.user.dto.request.UserSearchCondition
import kr.co.jsol.domain.entity.user.dto.request.UserUpdateRequest
import kr.co.jsol.domain.entity.user.dto.response.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
@Tag(name = "관리자 API", description = "계정 관리를 위한 API")
class AdminController(
    private val userService: UserService,
    private val siteService: SiteService,
) {

    @Operation(summary = "계정 생성")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "성공"),
        ApiResponse(responseCode = "400", description = "계정 생성에 실패했습니다.", content = [Content()])
    )
    @PostMapping("/user")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional
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

    @Operation(summary = "사이트 번호 중복 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
    )
    @GetMapping("/sites/exist/{siteSeq}")
    @ResponseStatus(value = HttpStatus.OK)
    fun isExistSiteSeq(@PathVariable siteSeq: Long): Boolean {
        return siteService.isExistSiteSeq(siteSeq)
    }

    @Operation(summary = "사용자 리스트 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다."),
        ApiResponse(responseCode = "403", description = "권한이 부족합니다.")
    )
    @GetMapping("/users")
    @ResponseStatus(value = HttpStatus.OK)
    fun getUserList(
        userSearchCondition: UserSearchCondition,
    ): List<UserResponse> {
        return userService.getUsers(userSearchCondition)
    }

    @Operation(summary = "사용자 상세 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다."),
        ApiResponse(responseCode = "403", description = "권한이 부족합니다."),
        ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다."),
    )
    @GetMapping("/users/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun getUser(@PathVariable id: String): UserResponse {
        return userService.getUser(id)
    }

    @Operation(summary = "사용자 수정")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.", content = [Content()])
    )
    @PutMapping("/user")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    fun putUser(@RequestBody userUpdateRequest: UserUpdateRequest): UserResponse {
        return userService.updateUser(userUpdateRequest)
    }

    @Operation(summary = "사용자 삭제")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공"),
        ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.", content = [Content()])
    )
    @DeleteMapping("/user/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    fun deleteUser(@PathVariable id: String): Boolean {
        return userService.deleteUserById(id)
    }
}
