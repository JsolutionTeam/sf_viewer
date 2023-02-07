package kr.co.jsol.api.config

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

    private val log = LoggerFactory.getLogger(SwaggerConfig::class.java)

    @Bean
    fun openAPI(@Value("\${springdoc.version:0.0.0}") appVersion: String?): OpenAPI {
        val localServer = Server()
        localServer.url = "http://localhost:18080"
        localServer.description = "Server URL in Local environment"

        val localServer2 = Server()
        localServer2.url = "http://localhost:18081"
        localServer2.description = "Server URL in Local environment test 2"

        val prodServer = Server()
        prodServer.url = "http://39.112.10.37:3000"
        prodServer.description = "Server URL in Production environment"

        val jwtScheme: SecurityScheme = SecurityScheme()
            .name("Authorization")
            .type(SecurityScheme.Type.HTTP)
            .`in`(SecurityScheme.In.HEADER)
            .bearerFormat("JWT")
            .scheme("bearer");

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
            .servers(
                listOf(
                    localServer, prodServer,
                    localServer2,
                )
            )
            .security(listOf(SecurityRequirement().addList("Authorization")))
    }

//    @Bean
//    fun securityItemCustomizer(): OpenApiCustomiser {
//        return OpenApiCustomiser { openApi: OpenAPI ->
//            openApi.paths.values.stream()
//                .map {
//                    log.info("path : {}", it)
//                    it
//                }
//                .flatMap { pathItem: PathItem ->
//                    pathItem.readOperations().stream()
//                }
//                .forEach { operation: Operation ->
//                    operation.addSecurityItem(
//                        SecurityRequirement().addList("Authorization")
//                    )
//                }
//        }
//    }

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
