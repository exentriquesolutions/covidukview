dependencies {
    implementation(project(":persistence"))

    implementation("commons-io:commons-io:2.8.0")
    implementation("org.springframework:spring-jdbc")
    implementation("org.flywaydb:flyway-core")

    testRuntimeOnly(files(project(":persistence").dependencyProject.sourceSets.main.get().output.classesDirs))
}
