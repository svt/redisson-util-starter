import org.assertj.assertions.generator.AssertionsEntryPointType.SOFT
import org.assertj.assertions.generator.AssertionsEntryPointType.STANDARD
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `idea`
    kotlin("jvm") version "1.6.21"
    kotlin("kapt") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"

    id("org.jmailen.kotlinter") version "3.10.0"
    id("com.github.fhermansson.assertj-generator") version "1.1.4"
    id("pl.allegro.tech.build.axion-release") version "1.13.9"
    id("se.ascp.gradle.gradle-versions-filter") version "0.1.16"
    id("se.svt.oss.gradle-yapp-publisher") version "0.1.18"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "se.svt.oss"
project.version = scmVersion.version

tasks.test {
    useJUnitPlatform()
}

dependencies {

    implementation("io.github.microutils:kotlin-logging:2.1.23")
    implementation("org.springframework.boot:spring-boot:2.7.0")
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.0")
    api("org.redisson:redisson:3.17.2")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.0")
    testImplementation("se.svt.oss:junit5-redis-extension:3.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("io.mockk:mockk:1.12.4")

}

kotlin {

    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11" //kaptGenerateStubsTestKotlin still needs this until Gradle 7.5. works without
        //generate warnings
    }
}

assertjGenerator {
    classOrPackageNames = arrayOf("se.svt.oss.redisson.util.starter.config")
    entryPointPackage = "se.svt.oss.redisson.util.starter"
    entryPointTypes = arrayOf(STANDARD, SOFT)
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.4.2"
}

