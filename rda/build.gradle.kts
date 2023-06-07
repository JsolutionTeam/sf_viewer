val querydslVersion: String by System.getProperties()

plugins {
    kotlin("kapt")
}

val kapt by configurations

dependencies {
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    kapt("org.springframework.boot:spring-boot-configuration-processor")
    kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa") // 이게 없으면 build해도 Q class가 생성되지 않는다.
}
tasks.withType<Jar> {
    enabled = false
}
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = true
}
