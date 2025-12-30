package fr.shiningcat.simplehiit.plugins

import fr.shiningcat.simplehiit.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for Compose navigation dependencies.
 * Provides Navigation 3 dependencies for UI feature modules with navigation support.
 */
class ComposeNavigationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            dependencies {
                // Navigation 3 core dependencies
                add("implementation", libs.findLibrary("androidx.navigation3.runtime").get())
                add("implementation", libs.findLibrary("androidx.navigation3.ui").get())
                // Serialization for type-safe navigation
                add("implementation", libs.findLibrary("kotlinx.serialization.core").get())
            }
        }
    }
}
