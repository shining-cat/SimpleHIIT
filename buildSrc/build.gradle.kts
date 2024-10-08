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
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    // This is to prevent the older version pulled by AGP to override the newer needed by Hilt
    implementation(libs.javapoet)
}
