package fr.shiningcat.simplehiit.plugins

import fr.shiningcat.simplehiit.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for Koin Compose integration.
 * Provides Koin dependencies for Compose UI modules that use koinViewModel().
 */
class KoinComposeConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            dependencies {
                add("implementation", libs.findLibrary("koin.androidx.compose").get())
                add("implementation", libs.findLibrary("koin.androidx.compose.navigation").get())
            }
        }
    }
}
