import org.gradle.kotlin.dsl.implementation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "tech.grimm"
description = "Discord bot"
version = "1.8"

plugins {
    application
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
}

application {

    mainClass.set("tech.grimm.midgard.MidgardKt")
}

repositories {
    mavenCentral()
}

dependencies {


    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("me.jakejmattson:DiscordKt:0.24.0")
    implementation("com.google.apis:google-api-services-youtube:v3-rev20220719-2.0.0")

    implementation("org.jetbrains.exposed", "exposed-core", "1.0.0-beta-5")
    implementation("org.jetbrains.exposed", "exposed-dao", "1.0.0-beta-5")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "1.0.0-beta-5")
    implementation("org.jetbrains.exposed", "exposed-java-time", "1.0.0-beta-5")
    implementation("org.xerial", "sqlite-jdbc", "3.50.3.0")

    implementation("com.openai", "openai-java", "4.0.0")


}


tasks.register<WriteProperties>("writeProperties") {
    dependsOn("processResources")

    property("name", project.name)
    property("description", project.description.toString())
    property("version", version.toString())
    property("url", "https://github.com/nullx27/midgard")
}

tasks.withType<Jar> {
    dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources"))

    archiveFileName.set("midgard.jar")

    manifest {
        attributes["Main-Class"] = application.mainClass
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(
        configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourceSets.main.get().output)
}