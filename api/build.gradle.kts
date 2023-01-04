dependencies {
    implementation(project(":domain"))
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("org.springframework.boot:spring-boot-starter-security")
    implementation("org.projectlombok:lombok:1.18.22")
}
