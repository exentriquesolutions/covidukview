import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val projectVersion = "1.0"

plugins {
    val kotlinVersion = "1.4.10"
    val springBootVersion = "2.3.2.RELEASE"

    id("org.jetbrains.kotlin.jvm").version(kotlinVersion)
    id("org.jetbrains.kotlin.plugin.spring").version(kotlinVersion)
    id("org.springframework.boot").version(springBootVersion)
    java
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

tasks.named<BootJar>("bootJar") {
    archiveBaseName.set(rootProject.name)
//    archiveVersion.set(project.version)
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
    implementation("commons-io:commons-io:2.8.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
    implementation("io.r2dbc:r2dbc-postgresql:0.8.5.RELEASE")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework:spring-jdbc")
    implementation("org.flywaydb:flyway-core")
    implementation("io.github.microutils:kotlin-logging:1.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // Use the Kotlin test library.
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.natpryce:hamkrest:1.8.0.1")
    testImplementation("io.r2dbc:r2dbc-h2:0.8.4.RELEASE")
}
