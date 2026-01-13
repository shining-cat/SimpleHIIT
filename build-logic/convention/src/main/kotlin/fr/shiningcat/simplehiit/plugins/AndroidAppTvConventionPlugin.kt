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
import java.io.File

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

                val signingKeystorePath = System.getenv("SIGNING_KEYSTORE_PATH")
                val signingKeystorePassword = System.getenv("SIGNING_KEYSTORE_PASSWORD")
                val signingKeyAlias = System.getenv("SIGNING_KEY_ALIAS")
                val signingKeyPassword = System.getenv("SIGNING_KEY_PASSWORD")

                if (signingKeystorePath != null && signingKeystorePassword != null) {
                    signingConfigs {
                        create("release") {
                            storeFile = File(signingKeystorePath)
                            storePassword = signingKeystorePassword
                            keyAlias = signingKeyAlias
                            keyPassword = signingKeyPassword
                        }
                    }
                }

                configureBuildTypes(this)

                if (signingKeystorePath != null) {
                    buildTypes {
                        getByName("release") {
                            signingConfig = signingConfigs.getByName("release")
                        }
                    }
                }
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
