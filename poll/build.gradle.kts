plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("kapt") version "1.9.23"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("com.google.protobuf") version "0.9.4"
    id("info.solidsoft.pitest") version "1.19.0-rc.1"

}

group = "org.voting"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
}

val mapStructVersion = "1.5.3.Final"
val testContainerVersion = "1.18.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
    implementation("org.mapstruct.extensions.spring:mapstruct-spring-annotations:0.1.2")
    implementation("org.mapstruct:mapstruct:$mapStructVersion")
    kapt("org.mapstruct:mapstruct-processor:$mapStructVersion")
    implementation("com.auth0:java-jwt:4.2.1")
    implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")
    implementation("io.grpc:grpc-protobuf:1.58.0")
    implementation("io.grpc:grpc-stub:1.58.0")
    implementation("io.grpc:grpc-kotlin-stub:1.4.0")
    implementation("com.google.protobuf:protobuf-kotlin:3.24.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.apache.poi:poi:5.2.4")
    implementation("org.apache.poi:poi-ooxml:5.2.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers:$testContainerVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainerVersion")
    testImplementation("org.testcontainers:mongodb:$testContainerVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    pitest("org.pitest:pitest-junit5-plugin:1.2.1")

}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.4"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.58.0"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.0:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}

sourceSets {
    main {
        proto {
            srcDir("src/main/proto/provide")
        }
    }
}

tasks.withType<com.google.protobuf.gradle.GenerateProtoTask> {
    dependsOn("extractIncludeProto")
}

tasks.jar {
    enabled = true
    manifest {
        attributes(
            "Implementation-Title" to "Poll Management Application",
            "Implementation-Version" to version
        )
    }
    archiveBaseName.set("poll")
    destinationDirectory.set(file("${layout.buildDirectory}/libs"))
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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
    targetClasses = setOf("org.voting.poll.domain.*")
    testPlugin = "junit5"
    outputFormats = setOf("XML", "HTML")
    verbose = true
    mutationThreshold = 0
    coverageThreshold = 0
}

