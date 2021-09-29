import org.assertj.assertions.generator.AssertionsEntryPointType.SOFT
import org.assertj.assertions.generator.AssertionsEntryPointType.STANDARD
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    `java-library`
    `signing`
    `idea`
    kotlin("jvm") version "1.5.21"
    kotlin("kapt") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
    kotlin("plugin.jpa") version "1.5.21"

    id("org.jmailen.kotlinter") version "3.6.0"
    id("com.waftex.assertj-generator") version "1.1.4"
    id("pl.allegro.tech.build.axion-release") version "1.13.3"
    id("se.ascp.gradle.gradle-versions-filter") version "0.1.10"
    id("se.svt.oss.gradle-yapp-publisher-plugin") version "0.1.15"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}

group = "se.svt.oss"
project.version = scmVersion.version

tasks.test {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("io.github.microutils:kotlin-logging:1.7.4")
    implementation("org.springframework.boot:spring-boot:2.2.6.RELEASE")
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.2.6.RELEASE")
    api("org.redisson:redisson:3.16.2")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.2.6.RELEASE")
    testImplementation("se.svt.oss:junit5-redis-extension:3.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("io.mockk:mockk:1.12.0")

}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

assertjGenerator {
    classOrPackageNames = arrayOf("se.svt.oss.redisson.util.starter.config")
    entryPointPackage = "se.svt.oss.redisson.util.starter"
    entryPointTypes = arrayOf(STANDARD, SOFT)
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.2"
}

