// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath ("com.android.tools.build:gradle:7.4.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10") // this is limited by kotlinCompilerExtensionVersion = "1.4.4"
        classpath ("com.squareup:javapoet:1.13.0")//This is to prevent the older version pulled by AGP to override the newer needed by Hilt
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("com.google.dagger.hilt.android") version Versions.hilt apply false
    // see files coverage.gradle.kts and CoveragePluginDSL.kts in buildSrc folder
    // article : https://medium.com/@gmazzo65/generating-android-jvm-aggregated-coverage-reports-53e912b2e63c
    //  source : https://github.com/gmazzo/android-jacoco-aggregated-demo
    coverage
    // to launch analysis of external dependencies versions, enter in the terminal
    // ./gradlew dependencyUpdates
    // for some reason the task is not added to the gradle tasks list in the IDE
    id("com.github.ben-manes.versions") version Versions.benManesDependenciesVersionPlugin
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

fun String.isNonStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return isStable.not()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        candidate.version.isNonStable()
    }
}
