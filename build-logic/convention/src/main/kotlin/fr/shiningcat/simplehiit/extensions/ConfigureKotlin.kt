package fr.shiningcat.simplehiit.extensions

import fr.shiningcat.simplehiit.config.ConfigHandheld
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

/**
 * Configure base Kotlin options
 */
internal inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() =
    configure<T> {
        // Treat all Kotlin warnings as errors (default to false)
        // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
        // this can be used to have it as true on local builds and false on CI builds (would require dedicated gradle.properties files)
        val warningsAsErrors: String? by project
        when (this) {
            is KotlinAndroidProjectExtension -> compilerOptions
            else -> error("Unsupported project extension $this ${T::class}")
        }.apply {
            jvmTarget.set(ConfigHandheld.jvm.kotlinJvm)
            allWarningsAsErrors.set(warningsAsErrors.toBoolean())
            freeCompilerArgs.addAll(ConfigHandheld.jvm.freeCompilerArgs)
            // Enable experimental coroutines APIs, including Flow
            freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
            // uncomment 2 options below for a (really) verbose gradle output:
            // freeCompilerArgs.add("-Xextended-compiler-checks")
            // verbose.set(true)
        }
    }
