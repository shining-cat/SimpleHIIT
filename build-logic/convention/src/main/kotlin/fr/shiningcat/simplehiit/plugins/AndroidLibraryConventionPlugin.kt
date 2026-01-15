/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.plugins

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import fr.shiningcat.simplehiit.config.SimpleHiitBuildType
import fr.shiningcat.simplehiit.extensions.configureAndroidLibraryKotlin
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

                // Library build types - libraries should NEVER minify, the app module would not find the symbols when it tries to consume the library
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                        enableUnitTestCoverage = SimpleHiitBuildType.RELEASE.enableUnitTestCoverage
                    }
                    getByName("debug") {
                        isMinifyEnabled = false
                        enableUnitTestCoverage = SimpleHiitBuildType.DEBUG.enableUnitTestCoverage
                    }
                }
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
