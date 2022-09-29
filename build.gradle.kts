import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "tech.grimm"
description = "Discord bot"
version = "0.1"

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("me.jakejmattson:DiscordKt:0.23.4")
    implementation("com.google.apis:google-api-services-youtube:v3-rev20220719-2.0.0")

    implementation("org.jetbrains.exposed", "exposed-core", "0.39.1")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.39.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.39.1")
    implementation("org.jetbrains.exposed", "exposed-java-time", "0.39.1")
    implementation("org.xerial", "sqlite-jdbc", "3.30.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    dependsOn("writeProperties")
}

tasks.register<WriteProperties>("writeProperties") {
    property("name", project.name)
    property("description", project.description.toString())
    property("version", version.toString())
    property("url", "https://github.com/nullx27/midgard")
    setOutputFile("src/main/resources/bot.properties")
}

tasks.jar {
    archiveFileName.set("midgard.jar")
}