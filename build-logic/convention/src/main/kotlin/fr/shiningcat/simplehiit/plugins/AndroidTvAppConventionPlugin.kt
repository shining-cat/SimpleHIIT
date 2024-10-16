package fr.shiningcat.simplehiit.plugins

import com.android.build.api.dsl.ApplicationExtension
import fr.shiningcat.simplehiit.config.ConfigTv
import fr.shiningcat.simplehiit.extensions.configureAndroidTvKotlin
import fr.shiningcat.simplehiit.extensions.configureBuildTypes
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidTvAppConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<ApplicationExtension> {
                configureAndroidTvKotlin(this)
                defaultConfig.apply {
                    targetSdk = ConfigTv.config.targetSdkVersion
                    applicationId = ConfigTv.config.applicationId
                    versionCode = ConfigTv.config.versionCode
                    versionName = ConfigTv.config.versionName
                }
                configureBuildTypes(this)
            }
        }
    }
}
