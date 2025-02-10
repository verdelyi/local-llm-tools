plugins {
    kotlin("jvm") version "2.1.10"
    application
    kotlin("plugin.serialization") version "2.1.10"
}

group = "localllm.ooba"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml
//    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.2")
//    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")

    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("localllm.ooba.Main")
    applicationDefaultJvmArgs = listOf("-Dsun.stdout.encoding=UTF-8", "-Dsun.stderr.encoding=UTF-8")
}