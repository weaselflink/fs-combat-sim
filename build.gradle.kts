@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val junit_version: String by project
val strikt_version: String by project
val java_version: String by project

plugins {
    application
    kotlin("jvm")
    id("com.adarshr.test-logger")
    id("com.github.ben-manes.versions")
}

group = "de.stefanbissell.fscombat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:$junit_version")
    testImplementation("io.strikt:strikt-core:$strikt_version")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(java_version))
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = java_version
        }
    }

    test {
        maxParallelForks = Runtime.getRuntime().availableProcessors()
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
