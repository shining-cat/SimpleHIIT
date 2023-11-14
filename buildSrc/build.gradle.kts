plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins` // this allows common gradle code extracted to be precompiled as a plugin to be applied to modules
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.1.3")
    implementation ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20") // this has to be kept in sync with kotlinCompilerExtension version. See https://developer.android.com/jetpack/androidx/releases/compose-kotlin for kotlin and compose compile version compatibility
    implementation("com.squareup:javapoet:1.13.0")//This is to prevent the older version pulled by AGP to override the newer needed by Hilt
}