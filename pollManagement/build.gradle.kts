plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("kapt") version "1.9.23"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("com.google.protobuf") version "0.9.4"
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
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.mapstruct.extensions.spring:mapstruct-spring-annotations:0.1.2")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
    implementation("net.devh:grpc-server-spring-boot-starter:2.14.0.RELEASE")
    implementation("io.grpc:grpc-protobuf:1.58.0")
    implementation("io.grpc:grpc-stub:1.58.0")
    implementation("io.grpc:grpc-netty-shaded:1.58.0")
    implementation("com.google.protobuf:protobuf-java:3.23.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers:1.18.1")
    testImplementation("org.testcontainers:junit-jupiter:1.18.1")
    testImplementation("org.testcontainers:mongodb:1.18.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks.jar {
    enabled = true
    manifest {
        attributes(
            "Implementation-Title" to "Poll Management Application",
            "Implementation-Version" to version
        )
    }
    archiveBaseName.set("poll-management")
    destinationDirectory.set(file("$buildDir/libs"))

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.23.4"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.58.0"
        }
    }
    generateProtoTasks {
        all().configureEach {
            plugins {
                named("grpc")
            }
        }
    }
}

sourceSets {
    main {
        proto {
            srcDir("protos")
        }
    }
}

detekt {
    basePath = projectDir.path
    config.setFrom("detekt.yml")
    buildUponDefaultConfig = false
    parallel = true

}