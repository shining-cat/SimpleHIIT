/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.extensions

import com.android.build.api.dsl.CommonExtension
import fr.shiningcat.simplehiit.config.ConfigTv
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureAndroidAppTvKotlin(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        namespace = ConfigTv.config.nameSpace
        compileSdk = ConfigTv.config.compileSdkVersion
        defaultConfig.apply {
            minSdk = ConfigTv.config.minSdkVersion
            testInstrumentationRunner = "fr.shiningcat.simplehiit.testutils.KoinTestRunner"
            vectorDrawables.useSupportLibrary = true
        }
        compileOptions {
            sourceCompatibility = ConfigTv.jvm.javaVersion
            targetCompatibility = ConfigTv.jvm.javaVersion
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
