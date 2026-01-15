/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.plugins

import fr.shiningcat.simplehiit.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

/**
 * Convention plugin for common testing dependencies and configuration.
 * Applies to all modules that need unit testing support.
 */
class TestingConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            dependencies {
                // Common testing dependencies for all modules
                add("testImplementation", libs.findLibrary("jupiter").get())
                add("testImplementation", libs.findLibrary("mockk").get())
                add("testImplementation", libs.findLibrary("jetbrains.coroutines.test").get())
            }

            // Android-specific test dependencies
            pluginManager.withPlugin("com.android.base") {
                dependencies {
                    add("testImplementation", libs.findLibrary("test.runner").get())
                }

                // Configure Android test options
                extensions.configure(com.android.build.gradle.BaseExtension::class.java) {
                    testOptions {
                        unitTests.all {
                            it.useJUnitPlatform()
                        }
                    }
                }
            }

            // Configure test tasks
            tasks.withType<Test>().configureEach {
                useJUnitPlatform()
                ignoreFailures = false
                outputs.upToDateWhen { false }
            }
        }
    }
}
