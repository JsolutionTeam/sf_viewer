dependencies {
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.projectlombok:lombok:1.18.22")

    // serializer, deserializer에서 사용.
    implementation("org.apache.commons:commons-lang3:3.12.0")


    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("org.springframework.integration:spring-integration-ip")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
