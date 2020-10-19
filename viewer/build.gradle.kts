import org.jetbrains.kotlin.ir.backend.js.compile

delete {
    delete("dist")
}

val npmInstall = task<Exec>("npmInstall") {
    commandLine = listOf("npm", "install")
}

val webpack = task<Exec>("webpack") {
    commandLine = listOf("node_modules/.bin/webpack")
    dependsOn(npmInstall)
}

tasks.compileTestKotlin {
    dependsOn(webpack)
}

dependencies {
    implementation(project(":persistence"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    testImplementation("com.flextrade.jfixture:kfixture:0.2.0")
    testImplementation("org.springframework:spring-jdbc")
    testImplementation("org.flywaydb:flyway-core")
    testImplementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    testImplementation("commons-io:commons-io:2.8.0")
    testRuntimeOnly(files(project(":persistence").dependencyProject.sourceSets.main.get().output.classesDirs))
    testRuntimeOnly(files(project(":persistence").dependencyProject.sourceSets.main.get().output.resourcesDir))
}
