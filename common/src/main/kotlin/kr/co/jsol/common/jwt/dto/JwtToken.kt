package kr.co.jsol.common.jwt.dto

data class JwtToken(val refreshToken: String, val accessToken: String)
