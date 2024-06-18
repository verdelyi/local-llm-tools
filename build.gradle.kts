plugins {
    kotlin("jvm") version "2.0.0"
    application
    kotlin("plugin.serialization") version "2.0.0"
}

group = "localllm.ooba"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("localllm.ooba.Main")
}