import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.projectlombok:lombok:1.18.22")

    // serializer, deserializer에서 사용.
    implementation("org.apache.commons:commons-lang3:3.12.0")

    implementation("org.springframework.boot:spring-boot-starter-validation")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-integration
    implementation("org.springframework.boot:spring-boot-starter-integration")
//    compileOnly("org.springframework.integration:spring-integration-core:6.0.2")
//    compileOnly("org.springframework.integration:spring-integration-event:6.0.2")
    compileOnly("org.springframework.integration:spring-integration-ip:6.0.2")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = true
jar.enabled = false
