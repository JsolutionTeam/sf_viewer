val querydslVersion: String by System.getProperties()

plugins {
    kotlin("kapt")
    kotlin("plugin.jpa")
}

dependencies {
    val kapt by configurations

    // // spring-boot-starter
    api("org.springframework.data:spring-data-commons")
    api("org.springframework.boot:spring-boot-starter-validation") // 파라미터 값 확인(인증, Bean Validation)을 위해
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-jdbc")

    // swagger v3
    api("org.springdoc:springdoc-openapi-ui:1.6.14")
    api("org.springdoc:springdoc-openapi-kotlin:1.6.14")

    // jwt
    implementation("org.apache.tomcat.embed:tomcat-embed-core:9.0.65") // exception filter에서 사용
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    api("io.jsonwebtoken:jjwt-gson:0.11.5") // implementation을 api로 변경하면 오류가 발생한다??
    api("io.jsonwebtoken:jjwt-api:0.11.5") // implementation을 api로 변경하면 오류가 발생한다??
}

tasks.withType<Jar> {
    enabled = true
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = false
}
