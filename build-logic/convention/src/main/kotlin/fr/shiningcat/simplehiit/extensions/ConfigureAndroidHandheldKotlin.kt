package fr.shiningcat.simplehiit.extensions

import com.android.build.api.dsl.CommonExtension
import fr.shiningcat.simplehiit.config.ConfigHandheld
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureAndroidHandheldKotlin(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        namespace = ConfigHandheld.config.nameSpace
        compileSdk = ConfigHandheld.config.compileSdkVersion
        defaultConfig.apply {
            minSdk = ConfigHandheld.config.minSdkVersion
            testInstrumentationRunner = "fr.shiningcat.simplehiit.testutils.HiltTestRunner"
            vectorDrawables.useSupportLibrary = true
        }
        compileOptions {
            sourceCompatibility = ConfigHandheld.jvm.javaVersion
            targetCompatibility = ConfigHandheld.jvm.javaVersion
        }
        packaging {
            resources {
                excludes.addAll(
                    listOf(
                        "META-INF/LICENSE.md",
                        "META-INF/LICENSE-notice.md",
                        "/META-INF/{AL2.0,LGPL2.1}",
                    ),
                )
            }
        }

        dependencies {
            add("implementation", libs.findLibrary("androidx.appcompat").get())
        }
    }
    configureKotlin<KotlinAndroidProjectExtension>()
}
