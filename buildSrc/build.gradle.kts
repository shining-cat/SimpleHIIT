plugins {
    `kotlin-dsl`
    // this allows common gradle code extracted to be precompiled as a plugin to be applied to modules
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.1.3")
    // this has to be kept in sync with kotlinCompilerExtension version. See https://developer.android.com/jetpack/androidx/releases/compose-kotlin for kotlin and compose compile version compatibility
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
    // This is to prevent the older version pulled by AGP to override the newer needed by Hilt
    implementation("com.squareup:javapoet:1.13.0")
}
