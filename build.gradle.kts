plugins {
    id("org.jetbrains.kotlin.jvm") version "2.2.20"
    id("org.jetbrains.kotlin.kapt") version "2.2.20"
    id("org.jetbrains.kotlin.plugin.allopen") version "2.2.20"
    id("org.jetbrains.kotlin.plugin.jpa") version "2.2.20"
    id("io.micronaut.application") version "4.5.4"
    id("com.gradleup.shadow") version "9.2.2"
    id("io.micronaut.test-resources") version "4.5.5"
    id("io.micronaut.aot") version "4.5.5"
    id("org.jetbrains.kotlin.plugin.noarg") version "2.2.20"
}

version = "0.1"
group = "com.comcode"

val kotlinVersion=project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

dependencies {
    kapt("io.micronaut.data:micronaut-data-processor")
    kapt("io.micronaut:micronaut-http-validation")
    kapt("org.hibernate.orm:hibernate-jpamodelgen")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")
    kapt("io.micronaut.security:micronaut-security-annotations")

    implementation("io.micronaut.security:micronaut-security")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
    implementation("io.micronaut.liquibase:micronaut-liquibase")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.beanvalidation:micronaut-hibernate-validator")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.data:micronaut-data-tx-hibernate")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.problem:micronaut-problem-json")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation("io.micronaut.sql:micronaut-hibernate-jpa")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.eclipse.angus:jakarta.mail:2.0.4")
    implementation("org.eclipse.angus:angus-mail:2.0.4")
    implementation("com.google.apis:google-api-services-gmail:v1-rev110-1.25.0")
    implementation("com.google.api-client:google-api-client:1.35.2")
    // https://mvnrepository.com/artifact/com.google.auth/google-auth-library-oauth2-http
    implementation("com.google.auth:google-auth-library-oauth2-http:1.37.1")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.liquibase:liquibase-core")
    runtimeOnly("org.eclipse.angus:angus-activation:2.0.2")

    annotationProcessor("io.micronaut.security:micronaut-security-annotations")
}

application {
    mainClass = "com.comcode.ApplicationKt"
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

noArg {
    annotation("jakarta.persistence.Entity") // gera construtor vazio para todas as @Entity
}

graalvmNative.toolchainDetection = false

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.comcode.*")
    }
    aot {
        // Please review carefully the optimizations enabled below
        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading = false
        convertYamlToJava = false
        precomputeOperations = true
        cacheEnvironment = true
        optimizeClassLoading = true
        deduceEnvironment = true
        optimizeNetty = true
        replaceLogbackXml = true
    }
}


tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}


