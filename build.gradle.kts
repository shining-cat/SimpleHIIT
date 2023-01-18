// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath ("com.android.tools.build:gradle:7.4.0")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath ("com.squareup:javapoet:1.13.0")//This is to prevent the older version pulled by AGP to override the newer needed by Hilt
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("com.google.dagger.hilt.android") version "2.44.2" apply false
    // see files coverage.gradle.kts and CoveragePluginDSL.kts in buildSrc folder
    // article : https://medium.com/@gmazzo65/generating-android-jvm-aggregated-coverage-reports-53e912b2e63c
    //  source : https://github.com/gmazzo/android-jacoco-aggregated-demo
    coverage
    //
    id("com.github.ben-manes.versions") version "0.43.0"
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}