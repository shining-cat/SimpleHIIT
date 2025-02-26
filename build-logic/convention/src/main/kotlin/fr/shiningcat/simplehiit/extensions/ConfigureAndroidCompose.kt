package fr.shiningcat.simplehiit.extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx.compose.bom").get()
            add(configurationName = "implementation", dependencyNotation = platform(bom))
            add(configurationName = "androidTestImplementation", dependencyNotation = platform(bom))
        }
    }
}
