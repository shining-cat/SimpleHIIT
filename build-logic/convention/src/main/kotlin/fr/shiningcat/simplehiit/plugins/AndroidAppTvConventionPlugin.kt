package fr.shiningcat.simplehiit.plugins

import com.android.build.api.dsl.ApplicationExtension
import fr.shiningcat.simplehiit.config.ConfigTv
import fr.shiningcat.simplehiit.extensions.configureAndroidAppTvKotlin
import fr.shiningcat.simplehiit.extensions.configureBuildTypes
import fr.shiningcat.simplehiit.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidAppTvConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jlleitschuh.gradle.ktlint")
            }
            extensions.configure<ApplicationExtension> {
                configureAndroidAppTvKotlin(this)
                defaultConfig.apply {
                    targetSdk = ConfigTv.config.targetSdkVersion
                    applicationId = ConfigTv.config.applicationId
                    versionCode = ConfigTv.config.versionCode
                    versionName = ConfigTv.config.versionName
                }
                configureBuildTypes(this)
            }
            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))

                // TV app-specific dependencies
                add("implementation", libs.findLibrary("androidx.lifecycle").get())
                add("implementation", libs.findLibrary("androidx.navigation3.runtime").get())
                add("implementation", libs.findLibrary("androidx.navigation3.ui").get())
                add("implementation", libs.findLibrary("androidx.tv.foundation").get())
                add("implementation", libs.findLibrary("androidx.tv.material").get())
                add("implementation", libs.findLibrary("koin.androidx.compose").get())
            }
        }
    }
}
