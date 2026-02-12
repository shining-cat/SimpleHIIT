/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
plugins {
    `kotlin-dsl`
}

group = "fr.shiningcat.simplehiit.buildlogic"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(JavaVersion.VERSION_21.majorVersion)
    }
}

kotlin {
    jvmToolchain(JavaVersion.VERSION_21.majorVersion.toInt())
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.kover.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidAppHandheld") {
            id = "fr.shiningcat.simplehiit.android.application.handheld"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.AndroidAppHandheldConventionPlugin"
        }
        register("androidAppTv") {
            id = "fr.shiningcat.simplehiit.android.application.tv"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.AndroidAppTvConventionPlugin"
        }
        register("androidLibrary") {
            id = "fr.shiningcat.simplehiit.android.library"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.AndroidLibraryConventionPlugin"
        }
        register("koin") {
            id = "fr.shiningcat.simplehiit.koin"
            implementationClass = "fr.shiningcat.simplehiit.plugins.KoinConventionPlugin"
        }
        register("koinCompose") {
            id = "fr.shiningcat.simplehiit.koin.compose"
            implementationClass = "fr.shiningcat.simplehiit.plugins.KoinComposeConventionPlugin"
        }
        register("androidAppCompose") {
            id = "fr.shiningcat.simplehiit.android.application.compose"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibCompose") {
            id = "fr.shiningcat.simplehiit.android.library.compose"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.AndroidLibraryComposeConventionPlugin"
        }
        register("documentation") {
            id = "fr.shiningcat.simplehiit.documentation"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.DocumentationConventionPlugin"
        }
        register("testing") {
            id = "fr.shiningcat.simplehiit.testing"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.TestingConventionPlugin"
        }
        register("composeNavigation") {
            id = "fr.shiningcat.simplehiit.compose.navigation"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.ComposeNavigationConventionPlugin"
        }
        register("mobileComposeUi") {
            id = "fr.shiningcat.simplehiit.mobile.compose.ui"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.MobileComposeUiConventionPlugin"
        }
        register("tvComposeUi") {
            id = "fr.shiningcat.simplehiit.tv.compose.ui"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.TvComposeUiConventionPlugin"
        }
    }
}
