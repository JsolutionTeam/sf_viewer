package kr.co.jsol.api.config;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Contact;

import java.util.Arrays;
import java.util.List;


@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {

        Server localServer = new Server();
        localServer.setUrl("http://localhost:18080");
        localServer.setDescription("Server URL in Local environment");

        Server prodServer = new Server();
        prodServer.setUrl("http://39.112.10.37:15006");
        prodServer.setDescription("Server URL in Production environment");

        License mitLicense = new License()
            .name("MIT License")
            .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
            .title("Jsolution 환경정보 모니터링 관리 API")
            .description("제이솔루션 2022 경북농기원 외주사업 APIDOC")
            .version("v0.0.1")
            .contact(new Contact().name("(주)제이솔루션").url("http://www.j-sol.co.kr").email("master@j-sol.co.kr"))
            .termsOfService("https://my-awesome-api.com/terms")
            .license(mitLicense);

        return new OpenAPI()
            .info(info)
            .servers(List.of(localServer, prodServer));
    }


    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
            .group("v1 user")
            .pathsToMatch("/api/**")
            .pathsToExclude("/api/admin/**")
            .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
            .group("v1 admin")
            .pathsToMatch("/api/admin/**", "/api/auth/**")
            .build();
    }

}
