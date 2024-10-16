package fr.shiningcat.simplehiit.plugins

import com.android.build.api.dsl.ApplicationExtension
import fr.shiningcat.simplehiit.config.ConfigHandheld
import fr.shiningcat.simplehiit.extensions.configureAndroidHandheldKotlin
import fr.shiningcat.simplehiit.extensions.configureBuildTypes
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidHandheldAppConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<ApplicationExtension> {
                configureAndroidHandheldKotlin(this)
                defaultConfig.apply {
                    targetSdk = ConfigHandheld.config.targetSdkVersion
                    applicationId = ConfigHandheld.config.applicationId
                    versionCode = ConfigHandheld.config.versionCode
                    versionName = ConfigHandheld.config.versionName
                }
                configureBuildTypes(this)
            }
        }
    }
}
