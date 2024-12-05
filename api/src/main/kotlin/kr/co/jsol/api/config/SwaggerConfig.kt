package kr.co.jsol.api.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.slf4j.LoggerFactory
import org.springdoc.core.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@io.swagger.v3.oas.annotations.security.SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
)
class SwaggerConfig {

    private val log = LoggerFactory.getLogger(SwaggerConfig::class.java)

    @Bean
    fun swagger(@Value("\${springdoc.version:0.0.0}") appVersion: String?): OpenAPI = OpenAPI().info(
        Info()
            .title("Jsolution 환경정보 모니터링 관리 API")
            .description("2022 제이솔루션 경북농기원 주문 제작 APIDOC")
            .version(appVersion) //            .termsOfService("http://none/terms/") // 이용 약관이 있을 경우 사용
            .contact(Contact().name("(주)제이솔루션").url("http://www.j-sol.co.kr").email("master@j-sol.co.kr"))
            .license(License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    )

    @Bean
    fun userApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("v1 user")
            .pathsToMatch("/api/**")
            .pathsToExclude("/api/admin/**")
            .build()
    }

    @Bean
    fun adminApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("v1 admin")
            .pathsToMatch("/api/admin/**", "/api/auth/**")
            .build()
    }
}
