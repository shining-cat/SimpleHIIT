/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.plugins

import fr.shiningcat.simplehiit.extensions.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

/**
 * Convention plugin for common testing dependencies and.
 * Applies to all modules that need unit testing support.
 */
class TestingConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            dependencies {
                // Common testing dependencies for all modules
                add("testImplementation", libs.findLibrary("jupiter").get())
                add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
                add("testImplementation", libs.findLibrary("mockk").get())
                add("testImplementation", libs.findLibrary("jetbrains.coroutines.test").get())
            }

            // Android-specific test dependencies and configuration
            pluginManager.withPlugin("com.android.library") {
                dependencies {
                    add("testImplementation", libs.findLibrary("androidx.test.runner").get())
                }

                // Configure Android test options for libraries
                extensions.configure(com.android.build.api.dsl.LibraryExtension::class.java) {
                    testOptions {
                        unitTests.all {
                            it.useJUnitPlatform()
                        }
                    }
                }
            }

            pluginManager.withPlugin("com.android.application") {
                dependencies {
                    add("testImplementation", libs.findLibrary("androidx.test.runner").get())
                }

                // Configure Android test options for applications
                extensions.configure(com.android.build.api.dsl.ApplicationExtension::class.java) {
                    testOptions {
                        unitTests.all {
                            it.useJUnitPlatform()
                        }
                    }
                }
            }

            // Configure test tasks
            val javaToolchains = extensions.getByType(JavaToolchainService::class.java)
            tasks.withType<Test>().configureEach {
                useJUnitPlatform()
                ignoreFailures = false
                outputs.upToDateWhen { false }

                // Set the JVM toolchain for the test runner
                javaLauncher.set(javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_21.majorVersion))
                })
            }
        }
    }
}
