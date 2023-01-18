import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.4.0")
    implementation ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    implementation("com.squareup:javapoet:1.13.0")//This is to prevent the older version pulled by AGP to override the newer needed by Hilt
}