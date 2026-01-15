/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.plugins

import fr.shiningcat.simplehiit.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for mobile Compose UI dependencies.
 * Provides common Material3, Adaptive, Preview, and Lifecycle dependencies for mobile UI modules.
 */
class MobileComposeUiConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            dependencies {
                // Material Design
                add("implementation", libs.findLibrary("google.material").get())
                add("implementation", libs.findLibrary("androidx.compose.material3").get())

                // Adaptive layout
                add("implementation", libs.findLibrary("androidx.compose.adaptive").get())

                // Compose preview
                add("implementation", libs.findLibrary("androidx.compose.preview").get())
                add("debugImplementation", libs.findLibrary("androidx.compose.preview.debug").get())
                add("debugImplementation", libs.findLibrary("androidx.compose.ui.test.debug").get())
            }
        }
    }
}
