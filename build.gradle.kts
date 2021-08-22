import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.5.30-RC"
    id("com.adarshr.test-logger") version "3.0.0"
}

group = "de.stefanbissell.fscombat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.0-RC1")
    testImplementation("io.strikt:strikt-core:0.31.0")
}

tasks {
    withType<KotlinCompile>() {
        kotlinOptions.jvmTarget = "11"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
