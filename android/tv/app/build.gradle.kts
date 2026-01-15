/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import fr.shiningcat.simplehiit.config.SimpleHiitBuildType

plugins {
    alias(libs.plugins.simplehiit.android.application.tv)
    alias(libs.plugins.simplehiit.koin)
    alias(libs.plugins.simplehiit.android.application.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
    alias(libs.plugins.modules.graph.assert)
}

android {
    buildTypes {
        debug {
            // applicationIdSuffix set from central enum config
            applicationIdSuffix = SimpleHiitBuildType.DEBUG.applicationIdSuffix
        }
    }
}

dependencies {
    implementation(projects.android.shared.core)
    implementation(projects.android.shared.home)
    implementation(projects.android.shared.session)
    implementation(projects.android.shared.settings)
    implementation(projects.android.shared.statistics)
    implementation(projects.android.tv.ui.common)
    implementation(projects.android.tv.ui.home)
    implementation(projects.android.tv.ui.settings)
    implementation(projects.android.tv.ui.session)
    implementation(projects.android.tv.ui.statistics)
    implementation(projects.models)
    implementation(projects.domain.common)
    implementation(projects.domain.home)
    implementation(projects.domain.session)
    implementation(projects.domain.settings)
    implementation(projects.domain.statistics)
    implementation(projects.sharedUi.home)
    implementation(projects.sharedUi.session)
    implementation(projects.sharedUi.settings)
    implementation(projects.sharedUi.statistics)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    implementation(projects.data)
    testImplementation(projects.testUtils)
}

moduleGraphAssert {
    maxHeight = 8

    allowed =
        arrayOf(
            // TV app can depend on anything (required by Android framework and DI)
            ":android:tv:app -> .*",
            // TV UI feature modules - CLEAN (models only, no domain)
            ":android:tv:ui:home -> :android:shared:core",
            ":android:tv:ui:home -> :android:shared:home",
            ":android:tv:ui:home -> :android:tv:ui:common",
            ":android:tv:ui:home -> :shared-ui:home",
            ":android:tv:ui:home -> :models",
            ":android:tv:ui:home -> :commonUtils",
            ":android:tv:ui:home -> :commonResources",
            //
            ":android:tv:ui:statistics -> :android:shared:core",
            ":android:tv:ui:statistics -> :android:shared:statistics",
            ":android:tv:ui:statistics -> :android:tv:ui:common",
            ":android:tv:ui:statistics -> :shared-ui:statistics",
            ":android:tv:ui:statistics -> :models",
            ":android:tv:ui:statistics -> :commonUtils",
            ":android:tv:ui:statistics -> :commonResources",
            //
            ":android:tv:ui:session -> :android:shared:core",
            ":android:tv:ui:session -> :android:shared:session",
            ":android:tv:ui:session -> :android:tv:ui:common",
            ":android:tv:ui:session -> :commonResources",
            ":android:tv:ui:session -> :commonUtils",
            ":android:tv:ui:session -> :models",
            ":android:tv:ui:session -> :shared-ui:session",
            //
            ":android:tv:ui:settings -> :android:shared:core",
            ":android:tv:ui:settings -> :android:shared:settings",
            ":android:tv:ui:settings -> :android:tv:ui:common",
            ":android:tv:ui:settings -> :shared-ui:settings",
            ":android:tv:ui:settings -> :models",
            ":android:tv:ui:settings -> :commonUtils",
            ":android:tv:ui:settings -> :commonResources",
            // TV UI common module - CLEAN (no domain dependencies)
            ":android:tv:ui:common -> :android:shared:core",
            ":android:tv:ui:common -> :commonResources",
            ":android:tv:ui:common -> :commonUtils",
            ":android:tv:ui:common -> :models",
            // shared-ui modules (KMP-ready: domain, models and utils only)
            ":shared-ui:.* -> :domain:.*",
            ":shared-ui:.* -> :models",
            ":shared-ui:.* -> :commonUtils",
            // Domain modules depend on models, domain:common, and commonUtils
            ":domain:home -> :models",
            ":domain:home -> :domain:common",
            ":domain:home -> :commonUtils",
            ":domain:session -> :models",
            ":domain:session -> :domain:common",
            ":domain:session -> :commonUtils",
            ":domain:settings -> :models",
            ":domain:settings -> :domain:common",
            ":domain:settings -> :commonUtils",
            ":domain:statistics -> :models",
            ":domain:statistics -> :domain:common",
            ":domain:statistics -> :commonUtils",
            // Domain common depends on models and commonUtils
            ":domain:common -> :models",
            ":domain:common -> :commonUtils",
            // Data depends on models, domain:common and commonUtils
            ":data -> :models",
            ":data -> :domain:common",
            ":data -> :commonUtils",
            // Common resources depends on models and domain:common
            ":commonResources -> :models",
            ":commonResources -> :domain:common",
            // Android shared:core - foundation module (navigation, theme, common UI)
            ":android:shared:core -> :commonResources",
            ":android:shared:core -> :commonUtils",
            ":android:shared:core -> :models",
            // Android shared:home - Thin ViewModel wrapper
            ":android:shared:home -> :commonUtils",
            ":android:shared:home -> :models",
            ":android:shared:home -> :shared-ui:home",
            // Android shared:session - Thin ViewModel wrapper
            ":android:shared:session -> :commonUtils",
            ":android:shared:session -> :shared-ui:session",
            // Android shared:settings - Thin ViewModel wrapper
            ":android:shared:settings -> :commonUtils",
            ":android:shared:settings -> :models",
            ":android:shared:settings -> :shared-ui:settings",
            // Android shared:statistics - Thin ViewModel wrapper
            ":android:shared:statistics -> :commonUtils",
            ":android:shared:statistics -> :models",
            ":android:shared:statistics -> :shared-ui:statistics",
            // Models module is foundation - no dependencies
            ":models -> (nothing allowed)",
        )

    restricted =
        arrayOf(
            // === DOMAIN LAYER RESTRICTIONS ===
            // Domain modules CANNOT depend on data layer
            ":domain:.* -X> :data",
            // Domain modules CANNOT depend on Android-specific modules
            ":domain:.* -X> :android:.*",
            ":domain:.* -X> :commonResources",
            // Domain modules CANNOT depend on UI modules
            ":domain:.* -X> :shared-ui:.*",
            // Domain modules CANNOT depend on app modules
            ":domain:.* -X> :android:mobile:app",
            ":domain:.* -X> :android:tv:app",
            // Domain modules CANNOT depend on other domain modules (except domain:common via allowed rules)
            ":domain:home -X> :domain:session",
            ":domain:home -X> :domain:settings",
            ":domain:home -X> :domain:statistics",
            ":domain:session -X> :domain:home",
            ":domain:session -X> :domain:settings",
            ":domain:session -X> :domain:statistics",
            ":domain:settings -X> :domain:home",
            ":domain:settings -X> :domain:session",
            ":domain:settings -X> :domain:statistics",
            ":domain:statistics -X> :domain:home",
            ":domain:statistics -X> :domain:session",
            ":domain:statistics -X> :domain:settings",
            // Data CANNOT depend on UI modules
            ":data -X> :android:tv:ui:.*",
            // Data CANNOT depend on app modules
            ":data -X> :android:tv:app",
            // Data CANNOT depend on other domain modules (only domain:common via allowed rules)
            ":data -X> :domain:home",
            ":data -X> :domain:session",
            ":data -X> :domain:settings",
            ":data -X> :domain:statistics",
            // shared-ui CANNOT depend on Android-specific modules (KMP-ready)
            ":shared-ui:.* -X> :android:.*",
            // shared-ui CANNOT depend on commonResources (Android-specific)
            ":shared-ui:.* -X> :commonResources",
            // === ANDROID SHARED LAYER RESTRICTIONS ===
            // android:shared modules CANNOT depend on domain layer directly (must go through shared-ui)
            ":android:shared:home -X> :domain:.*",
            ":android:shared:session -X> :domain:.*",
            ":android:shared:settings -X> :domain:.*",
            ":android:shared:statistics -X> :domain:.*",
            ":android:shared:core -X> :domain:.*",
            // android:shared modules CANNOT depend on data layer
            ":android:shared:.* -X> :data",
            // android:shared feature modules CANNOT depend on other feature modules
            ":android:shared:home -X> :android:shared:session",
            ":android:shared:home -X> :android:shared:settings",
            ":android:shared:home -X> :android:shared:statistics",
            ":android:shared:session -X> :android:shared:home",
            ":android:shared:session -X> :android:shared:settings",
            ":android:shared:session -X> :android:shared:statistics",
            ":android:shared:settings -X> :android:shared:home",
            ":android:shared:settings -X> :android:shared:session",
            ":android:shared:settings -X> :android:shared:statistics",
            ":android:shared:statistics -X> :android:shared:home",
            ":android:shared:statistics -X> :android:shared:session",
            ":android:shared:statistics -X> :android:shared:settings",
            // android:shared:core CANNOT depend on feature modules
            ":android:shared:core -X> :android:shared:home",
            ":android:shared:core -X> :android:shared:session",
            ":android:shared:core -X> :android:shared:settings",
            ":android:shared:core -X> :android:shared:statistics",
            ":android:shared:core -X> :shared-ui:.*",
            ":android:shared:core -X> :data",
            // === ANDROID UI LAYER RESTRICTIONS ===
            // UI modules CANNOT depend on data layer
            ":android:tv:ui:.* -X> :data",
            // UI modules CANNOT depend on domain feature modules
            ":android:tv:ui:home -X> :domain:home",
            ":android:tv:ui:home -X> :domain:session",
            ":android:tv:ui:home -X> :domain:settings",
            ":android:tv:ui:home -X> :domain:statistics",
            ":android:tv:ui:statistics -X> :domain:home",
            ":android:tv:ui:statistics -X> :domain:session",
            ":android:tv:ui:statistics -X> :domain:settings",
            ":android:tv:ui:statistics -X> :domain:statistics",
            ":android:tv:ui:session -X> :domain:home",
            ":android:tv:ui:session -X> :domain:session",
            ":android:tv:ui:session -X> :domain:settings",
            ":android:tv:ui:session -X> :domain:statistics",
            ":android:tv:ui:settings -X> :domain:home",
            ":android:tv:ui:settings -X> :domain:session",
            ":android:tv:ui:settings -X> :domain:settings",
            ":android:tv:ui:settings -X> :domain:statistics",
            ":android:tv:ui:common -X> :domain:home",
            ":android:tv:ui:common -X> :domain:session",
            ":android:tv:ui:common -X> :domain:settings",
            ":android:tv:ui:common -X> :domain:statistics",
            // UI feature modules CANNOT depend on other feature UI modules
            ":android:tv:ui:home -X> :android:tv:ui:session",
            ":android:tv:ui:home -X> :android:tv:ui:settings",
            ":android:tv:ui:home -X> :android:tv:ui:statistics",
            ":android:tv:ui:session -X> :android:tv:ui:home",
            ":android:tv:ui:session -X> :android:tv:ui:settings",
            ":android:tv:ui:session -X> :android:tv:ui:statistics",
            ":android:tv:ui:settings -X> :android:tv:ui:home",
            ":android:tv:ui:settings -X> :android:tv:ui:session",
            ":android:tv:ui:settings -X> :android:tv:ui:statistics",
            ":android:tv:ui:statistics -X> :android:tv:ui:home",
            ":android:tv:ui:statistics -X> :android:tv:ui:session",
            ":android:tv:ui:statistics -X> :android:tv:ui:settings",
            // UI modules CANNOT depend on other feature ViewModels
            ":android:tv:ui:home -X> :android:shared:session",
            ":android:tv:ui:home -X> :android:shared:settings",
            ":android:tv:ui:home -X> :android:shared:statistics",
            ":android:tv:ui:session -X> :android:shared:home",
            ":android:tv:ui:session -X> :android:shared:settings",
            ":android:tv:ui:session -X> :android:shared:statistics",
            ":android:tv:ui:settings -X> :android:shared:home",
            ":android:tv:ui:settings -X> :android:shared:session",
            ":android:tv:ui:settings -X> :android:shared:statistics",
            ":android:tv:ui:statistics -X> :android:shared:home",
            ":android:tv:ui:statistics -X> :android:shared:session",
            ":android:tv:ui:statistics -X> :android:shared:settings",
            // === DATA LAYER RESTRICTIONS ===
            // Data CANNOT depend on Android modules
            ":data -X> :android:.*",
            ":data -X> :commonResources",
            // Data CANNOT depend on shared-ui
            ":data -X> :shared-ui:.*",
            // === ANDROID RESOURCES LAYER RESTRICTIONS ===
            // commonResources CANNOT depend on Android/UI/Data layers
            ":commonResources -X> :android:.*",
            ":commonResources -X> :data",
            ":commonResources -X> :shared-ui:.*",
            // === FOUNDATION MODULE RESTRICTIONS ===
            // Models CANNOT depend on anything
            ":models -X> :.*",
            // NO module can depend on TV app module
            ":android:tv:ui:.* -X> :android:tv:app",
            ":shared-ui:.* -X> :android:tv:app",
            ":commonUtils -X> :android:tv:app",
            ":commonResources -X> :android:tv:app",
            ":android:shared:core -X> :android:tv:app",
            ":testUtils -X> :android:tv:app",
            // commonUtils CANNOT depend on anything (foundation module)
            ":commonUtils -X> :.*",
            // testUtils CANNOT depend on anything (isolated testing)
            ":testUtils -X> :.*",
        )

    configurations = setOf("api", "implementation")
}
