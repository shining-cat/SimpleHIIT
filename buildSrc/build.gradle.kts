import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.0.2")
    implementation ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21") // this is limited by kotlinCompilerExtensionVersion = "1.4.4"
    implementation("com.squareup:javapoet:1.13.0")//This is to prevent the older version pulled by AGP to override the newer needed by Hilt
}