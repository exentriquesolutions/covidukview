import org.springframework.boot.gradle.tasks.bundling.BootJar

val projectVersion = "1.0"
val projectName = "coviduk"

plugins {
    val kotlinVersion = "1.3.21"
    val springBootVersion = "2.3.2.RELEASE"

    id("org.jetbrains.kotlin.jvm").version(kotlinVersion)
    id("org.jetbrains.kotlin.plugin.spring").version(kotlinVersion)
    id("org.springframework.boot").version(springBootVersion)
}

apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

tasks.named<BootJar>("bootJar") {
    archiveBaseName.set(projectName)
    archiveVersion.set(projectVersion)
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

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}
