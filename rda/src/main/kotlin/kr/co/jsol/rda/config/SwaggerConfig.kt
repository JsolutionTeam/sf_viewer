package kr.co.jsol.rda.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.slf4j.LoggerFactory
import org.springdoc.core.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
@Component
class SwaggerConfig {
    @Bean
    fun openAPI(@Value("\${springdoc.version:0.0.0}") appVersion: String?): OpenAPI {
        val jwtScheme: SecurityScheme = SecurityScheme()
            .name("Authorization")
            .type(SecurityScheme.Type.HTTP)
            .`in`(SecurityScheme.In.HEADER)
            .bearerFormat("JWT")
            .scheme("bearer")
        val info = Info()
            .title("Jsolution 환경정보 모니터링 관리 API")
            .description("제이솔루션 2022 경북농기원 외주사업 APIDOC")
            .version(appVersion) //            .termsOfService("http://none/terms/") // 이용 약관이 있을 경우 사용
            .contact(Contact().name("(주)제이솔루션").url("http://www.j-sol.co.kr").email("master@j-sol.co.kr"))
            .license(License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"))
        return OpenAPI()
            .components(
                Components().addSecuritySchemes(
                    "Authorization", jwtScheme
                )
            )
            .info(info)
            .security(listOf(SecurityRequirement().addList("Authorization")))
    }
}
