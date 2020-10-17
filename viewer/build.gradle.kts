dependencies {
    implementation(project(":persistence"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    testImplementation("com.flextrade.jfixture:kfixture:0.2.0")
    testImplementation("org.springframework:spring-jdbc")
    testImplementation("org.flywaydb:flyway-core")
    testRuntimeOnly(files(project(":persistence").dependencyProject.sourceSets.main.get().output.classesDirs))
    testRuntimeOnly(files(project(":persistence").dependencyProject.sourceSets.main.get().output.resourcesDir))
}
