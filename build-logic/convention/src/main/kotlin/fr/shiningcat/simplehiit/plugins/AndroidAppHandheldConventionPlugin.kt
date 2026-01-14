package fr.shiningcat.simplehiit.plugins

import com.android.build.api.dsl.ApplicationExtension
import fr.shiningcat.simplehiit.config.ConfigHandheld
import fr.shiningcat.simplehiit.config.SimpleHiitBuildType
import fr.shiningcat.simplehiit.extensions.configureAndroidAppHandheldKotlin
import fr.shiningcat.simplehiit.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import java.io.File

class AndroidAppHandheldConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jlleitschuh.gradle.ktlint")
            }
            extensions.configure<ApplicationExtension> {
                configureAndroidAppHandheldKotlin(this)
                defaultConfig.apply {
                    targetSdk = ConfigHandheld.config.targetSdkVersion
                    applicationId = ConfigHandheld.config.applicationId
                    versionCode = ConfigHandheld.config.versionCode
                    versionName = ConfigHandheld.config.versionName
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

                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = SimpleHiitBuildType.RELEASE.isMinifyEnabled
                        isShrinkResources = true
                        enableUnitTestCoverage = SimpleHiitBuildType.RELEASE.enableUnitTestCoverage
                        if (signingKeystorePath != null) {
                            signingConfig = signingConfigs.getByName("release")
                        }
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro",
                        )
                    }
                    getByName("debug") {
                        isMinifyEnabled = SimpleHiitBuildType.DEBUG.isMinifyEnabled
                        enableUnitTestCoverage = SimpleHiitBuildType.DEBUG.enableUnitTestCoverage
                        applicationIdSuffix = SimpleHiitBuildType.DEBUG.applicationIdSuffix
                    }
                }
            }
            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))

                // Mobile app-specific dependencies
                add("implementation", libs.findLibrary("androidx.compose.material3").get())
                add("implementation", libs.findLibrary("androidx.compose.adaptive").get())
                add("implementation", libs.findLibrary("androidx.lifecycle").get())
                add("implementation", libs.findLibrary("androidx.navigation3.runtime").get())
                add("implementation", libs.findLibrary("androidx.navigation3.ui").get())
                add("implementation", libs.findLibrary("androidx.activity.compose").get())
                add("implementation", libs.findLibrary("koin.androidx.compose").get())
            }
        }
    }
}
