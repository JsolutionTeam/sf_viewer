package kr.co.jsol.api.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30) // open api spec 3.0
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build();
//    }

    /**
     * ocket: Swagger 설정의 핵심이 되는 Bean
     * useDefaultResponseMessages: Swagger 에서 제공해주는 기본 응답 코드 (200, 401, 403, 404). false 로 설정하면 기본 응답 코드를 노출하지 않음
     * apis: api 스펙이 작성되어 있는 패키지 (Controller) 를 지정
     * paths: apis 에 있는 API 중 특정 path 를 선택
     * apiInfo:Swagger UI 로 노출할 정보
     */
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30) // OAS 3
//                .useDefaultResponseMessages(false)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("")) // controller가 있는 패키지 지정 basePackage
//                // Application main 있는 파일에서 지정해줌.
////                .apis(RequestHandlerSelectors.any()) // controller가 있는 패키지 지정 basePackage
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30) // OAS 3
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("kr.co.jsol.api.controller")) // controller가 있는 패키지 지정 basePackage
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SmartFarm viewer API Swagger")
                .description("2022-23-01 api docs")
                .version("1.2")
                .build();
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }
}
