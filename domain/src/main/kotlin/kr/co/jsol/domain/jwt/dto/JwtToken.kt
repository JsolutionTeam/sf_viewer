package kr.co.jsol.domain.jwt.dto

data class JwtToken(val refreshToken: String, val accessToken: String)
