dependencies {
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}
tasks.withType<Jar> {
    enabled = false
}
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = true
}
