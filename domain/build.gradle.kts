import org.springframework.boot.gradle.tasks.bundling.BootJar

val querydslVersion: String by System.getProperties()

plugins {
    kotlin("kapt")
    kotlin("plugin.jpa")

    // intellij idea에서 사용할 수 있도록 추가
    idea
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

noArg {
    annotation("javax.persistence.Entity") // @Entity가 붙은 클래스에 한해서만 no arg 플러그인을 적용
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

dependencies {
    api(project(":common"))

    val kapt by configurations

    // // spring-boot-starter
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.0")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // querydsl kotlin codegen
    api("com.querydsl:querydsl-jpa:$querydslVersion")
    api("com.querydsl:querydsl-kotlin-codegen:$querydslVersion") // kotlin code generation support

    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.1")

    //apt
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa") // 이게 없으면 build해도 Q class가 생성되지 않는다.

    // test
    testApi("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Jar> {
    enabled = true
    // build 중에 중복되는 파일이 생성될경우 에러가 발생한다. 그것을 방지하기 위한 설정이다.
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<BootJar> {
    enabled = false
}
