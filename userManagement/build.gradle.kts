plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("kapt") version "1.9.23"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("info.solidsoft.pitest") version "1.19.0-rc.1"
}

group = "org.voting"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.mapstruct.extensions.spring:mapstruct-spring-annotations:0.1.2")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
    implementation("com.auth0:java-jwt:4.2.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers:1.18.1")
    testImplementation("org.testcontainers:junit-jupiter:1.18.1")
    testImplementation("org.testcontainers:mongodb:1.18.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    pitest("org.pitest:pitest-junit5-plugin:1.2.1")

}

tasks.jar {
    enabled = true
    manifest {
        attributes(
            "Implementation-Title" to "User Management Application",
            "Implementation-Version" to version
        )
    }
    archiveBaseName.set("user-management")
    destinationDirectory.set(file("${layout.buildDirectory}/libs"))

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

detekt {
    basePath = projectDir.path
    config.setFrom("detekt.yml")
    buildUponDefaultConfig = false
    parallel = true
}

pitest {
    targetClasses = setOf("org.voting.usermanagement.domain.*")
    testPlugin = "junit5"
    outputFormats = setOf("XML", "HTML")
    verbose = true
    mutationThreshold = 0
    coverageThreshold = 0
}
