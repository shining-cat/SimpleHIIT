package fr.shiningcat.simplehiit.config

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// When deploying the same app for 2 different platforms, each app needs to have their own versionCode,
// but a common applicationId (sometimes called application's package name)
// Here we're keeping definitions for both so they can be compared to each other

// VersionCode scheme: XXYYZZZZ where
//      XX represents the min API level supported
//      YY represents the targeted device family:
//          10 for handheld devices (uses-feature touchscreen required)
//          01 for TV devices (uses-feature leanback required, touchscreen not required)
//      ZZZZ is the compacted versionName so that app version 2.34 will be 0234

// common applicationId
const val APPLICATION_ID = "fr.shiningcat.simplehiit"

object ConfigHandheld {
    val config =
        AndroidConfig(
            minSdkVersion = 21,
            targetSdkVersion = 34,
            compileSdkVersion = 34,
            applicationId = APPLICATION_ID,
            versionCode = 21100003,
            versionName = "0.03",
            nameSpace = "fr.shiningcat.simplehiit.mobile.app",
        )
    val jvm =
        JvmConfig(
            javaVersion = JavaVersion.VERSION_17,
            kotlinJvm = JvmTarget.JVM_17,
        )
}

object ConfigTv {
    val config =
        AndroidConfig(
            minSdkVersion = 21,
            targetSdkVersion = 34,
            compileSdkVersion = 34,
            applicationId = APPLICATION_ID,
            versionCode = 21010003,
            versionName = "0.03",
            nameSpace = "fr.shiningcat.simplehiit.tv.app",
        )
    val jvm =
        JvmConfig(
            javaVersion = JavaVersion.VERSION_17,
            kotlinJvm = JvmTarget.JVM_17,
        )
}

object ConfigLibrary {
    // libraries config data: these should be the lowest of tv and handheld respective values
    val config =
        AndroidLibraryConfig(
            minSdkVersion = 21,
            compileSdkVersion = 34,
        )
    val jvm =
        JvmConfig(
            javaVersion = JavaVersion.VERSION_17,
            kotlinJvm = JvmTarget.JVM_17,
        )
}

data class AndroidLibraryConfig(
    val minSdkVersion: Int,
    val compileSdkVersion: Int,
)

data class AndroidConfig(
    val minSdkVersion: Int,
    val targetSdkVersion: Int,
    val compileSdkVersion: Int,
    val applicationId: String,
    val versionCode: Int,
    val versionName: String,
    val nameSpace: String,
)

data class JvmConfig(
    val javaVersion: JavaVersion,
    val kotlinJvm: JvmTarget,
    val freeCompilerArgs: List<String> = emptyList(),
)
