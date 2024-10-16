package fr.shiningcat.simplehiit.extensions

import com.android.build.api.dsl.CommonExtension
import fr.shiningcat.simplehiit.config.ConfigLibrary
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureAndroidLibraryKotlin(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = ConfigLibrary.config.compileSdkVersion
        defaultConfig.apply {
            minSdk = ConfigLibrary.config.minSdkVersion
            testInstrumentationRunner = "fr.shiningcat.simplehiit.testutils.HiltTestRunner"
            testOptions.animationsDisabled = true
            vectorDrawables.useSupportLibrary = true
        }
        compileOptions {
            sourceCompatibility = ConfigLibrary.jvm.javaVersion
            targetCompatibility = ConfigLibrary.jvm.javaVersion
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
    }
    configureKotlin<KotlinAndroidProjectExtension>()
}
