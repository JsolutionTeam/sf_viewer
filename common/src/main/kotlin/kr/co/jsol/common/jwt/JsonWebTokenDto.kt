package kr.co.jsol.common.jwt

data class JsonWebTokenDto(
    val grantType: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val accessTokenExpiresIn: Long? = null,
)
