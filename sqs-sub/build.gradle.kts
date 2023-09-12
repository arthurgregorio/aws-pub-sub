import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"

    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

group = "br.eti.arthurgregorio"
version = "0.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val commonsLangVersion = "3.12.0"
val awsSpringVersion = "3.0.1"
val kotlinLoggingJvmVersion = "4.0.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")

    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.apache.commons:commons-lang3:$commonsLangVersion")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:$awsSpringVersion")
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JVM_17)
        languageVersion.set(KOTLIN_1_8)
        freeCompilerArgs.set(
            listOf(
                "-Xjsr305=strict",
                "-Xjdk-release=${java.sourceCompatibility}"
            )
        )
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}
