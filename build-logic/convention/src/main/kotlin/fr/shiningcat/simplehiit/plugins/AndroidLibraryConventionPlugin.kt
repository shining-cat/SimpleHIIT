package fr.shiningcat.simplehiit.plugins

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import fr.shiningcat.simplehiit.extensions.configureAndroidLibraryKotlin
import fr.shiningcat.simplehiit.extensions.configureBuildTypes
import fr.shiningcat.simplehiit.extensions.disableUnnecessaryAndroidTests
import fr.shiningcat.simplehiit.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
                apply("org.jlleitschuh.gradle.ktlint")
            }
            extensions.configure<LibraryExtension> {
                configureAndroidLibraryKotlin(this)
                configureBuildTypes(this)
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(project)
            }
            dependencies {
                // Lifecycle - needed by ViewModels across all Android library modules
                add("implementation", libs.findLibrary("androidx.lifecycle").get())
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))
            }
        }
    }
}
