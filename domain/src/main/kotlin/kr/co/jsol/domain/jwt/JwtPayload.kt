package kr.co.jsol.domain.jwt

import io.jsonwebtoken.Claims

class JwtPayload(body: Claims) {
    // 아이디
    val sub: String

    // 이름
    val name: String

    // 권한
    val role: String

    // 작성자
    val iss: String

    // 생성일
    val iat: Long

    // 만료일
    val exp: Long

    init {
        sub = body["sub"].toString()
        name = body["name"].toString()
        role = body["role"].toString()
        iss = body["iss"].toString()
        iat = body["iat"].toString().toLong()
        exp = body["exp"].toString().toLong()
    }
}
