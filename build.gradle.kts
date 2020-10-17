plugins {
    base
    kotlin("jvm") version "1.4.10" apply false
    id("org.jetbrains.kotlin.plugin.spring").version("1.4.10")
    id("org.springframework.boot").version("2.3.2.RELEASE")
    java
}

subprojects {
    group = "com.exentriquesolutions.covidukview"
    version = "1.0"

    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.springframework.boot")
    }

    repositories {
        jcenter()
        mavenCentral()
    }

    val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
    compileKotlin.kotlinOptions.jvmTarget = "11"
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar")

    tasks.test {
        classpath = sourceSets.main.get().runtimeClasspath + classpath - files(tasks.jar)
        useJUnitPlatform()
    }

    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    dependencies {
        implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
        implementation("io.r2dbc:r2dbc-postgresql:0.8.5.RELEASE")
        implementation("org.postgresql:postgresql")
        implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
        implementation("io.github.microutils:kotlin-logging:1.12.0")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("com.natpryce:hamkrest:1.8.0.1")
        testImplementation("io.r2dbc:r2dbc-h2:0.8.4.RELEASE")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.4.2")
    }
}
