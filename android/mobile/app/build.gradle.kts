/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import fr.shiningcat.simplehiit.config.SimpleHiitBuildType

plugins {
    alias(libs.plugins.simplehiit.android.application.handheld)
    alias(libs.plugins.simplehiit.koin)
    alias(libs.plugins.simplehiit.android.application.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
    alias(libs.plugins.modules.graph.assert)
}

android {
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

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
    implementation(projects.android.mobile.ui.common)
    implementation(projects.android.mobile.ui.home)
    implementation(projects.android.mobile.ui.session)
    implementation(projects.android.mobile.ui.settings)
    implementation(projects.android.mobile.ui.statistics)
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
            // Mobile app can depend on anything (required by Android framework and DI)
            ":android:mobile:app -> .*",
            // Mobile UI feature modules - CLEAN (models only, no domain)
            ":android:mobile:ui:home -> :android:shared:core",
            ":android:mobile:ui:home -> :android:shared:home",
            ":android:mobile:ui:home -> :android:mobile:ui:common",
            ":android:mobile:ui:home -> :shared-ui:home",
            ":android:mobile:ui:home -> :models",
            ":android:mobile:ui:home -> :commonUtils",
            ":android:mobile:ui:home -> :commonResources",
            ":android:mobile:ui:statistics -> :android:shared:core",
            ":android:mobile:ui:statistics -> :android:shared:statistics",
            ":android:mobile:ui:statistics -> :android:mobile:ui:common",
            ":android:mobile:ui:statistics -> :shared-ui:statistics",
            ":android:mobile:ui:statistics -> :models",
            ":android:mobile:ui:statistics -> :commonUtils",
            ":android:mobile:ui:statistics -> :commonResources",
            // Mobile UI feature modules - CLEAN (models only, no domain)
            ":android:mobile:ui:session -> :android:mobile:ui:common",
            ":android:mobile:ui:session -> :android:shared:core",
            ":android:mobile:ui:session -> :android:shared:session",
            ":android:mobile:ui:session -> :commonResources",
            ":android:mobile:ui:session -> :commonUtils",
            ":android:mobile:ui:session -> :models",
            ":android:mobile:ui:session -> :shared-ui:session",
            ":android:mobile:ui:settings -> :android:shared:core",
            ":android:mobile:ui:settings -> :android:shared:settings",
            ":android:mobile:ui:settings -> :android:mobile:ui:common",
            ":android:mobile:ui:settings -> :shared-ui:settings",
            ":android:mobile:ui:settings -> :models",
            ":android:mobile:ui:settings -> :commonUtils",
            ":android:mobile:ui:settings -> :commonResources",
            // Mobile UI common module - CLEAN (no domain dependencies)
            ":android:mobile:ui:common -> :android:shared:core",
            ":android:mobile:ui:common -> :commonResources",
            ":android:mobile:ui:common -> :commonUtils",
            ":android:mobile:ui:common -> :models",
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
            ":data -X> :android:mobile:ui:.*",
            // Data CANNOT depend on app modules
            ":data -X> :android:mobile:app",
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
            ":android:mobile:ui:.* -X> :data",
            // UI modules CANNOT depend on domain feature modules
            ":android:mobile:ui:home -X> :domain:home",
            ":android:mobile:ui:home -X> :domain:session",
            ":android:mobile:ui:home -X> :domain:settings",
            ":android:mobile:ui:home -X> :domain:statistics",
            ":android:mobile:ui:statistics -X> :domain:home",
            ":android:mobile:ui:statistics -X> :domain:session",
            ":android:mobile:ui:statistics -X> :domain:settings",
            ":android:mobile:ui:statistics -X> :domain:statistics",
            ":android:mobile:ui:session -X> :domain:home",
            ":android:mobile:ui:session -X> :domain:session",
            ":android:mobile:ui:session -X> :domain:settings",
            ":android:mobile:ui:session -X> :domain:statistics",
            ":android:mobile:ui:settings -X> :domain:home",
            ":android:mobile:ui:settings -X> :domain:session",
            ":android:mobile:ui:settings -X> :domain:settings",
            ":android:mobile:ui:settings -X> :domain:statistics",
            ":android:mobile:ui:common -X> :domain:home",
            ":android:mobile:ui:common -X> :domain:session",
            ":android:mobile:ui:common -X> :domain:settings",
            ":android:mobile:ui:common -X> :domain:statistics",
            // UI feature modules CANNOT depend on other feature UI modules
            ":android:mobile:ui:home -X> :android:mobile:ui:session",
            ":android:mobile:ui:home -X> :android:mobile:ui:settings",
            ":android:mobile:ui:home -X> :android:mobile:ui:statistics",
            ":android:mobile:ui:session -X> :android:mobile:ui:home",
            ":android:mobile:ui:session -X> :android:mobile:ui:settings",
            ":android:mobile:ui:session -X> :android:mobile:ui:statistics",
            ":android:mobile:ui:settings -X> :android:mobile:ui:home",
            ":android:mobile:ui:settings -X> :android:mobile:ui:session",
            ":android:mobile:ui:settings -X> :android:mobile:ui:statistics",
            ":android:mobile:ui:statistics -X> :android:mobile:ui:home",
            ":android:mobile:ui:statistics -X> :android:mobile:ui:session",
            ":android:mobile:ui:statistics -X> :android:mobile:ui:settings",
            // UI modules CANNOT depend on other feature ViewModels
            ":android:mobile:ui:home -X> :android:shared:session",
            ":android:mobile:ui:home -X> :android:shared:settings",
            ":android:mobile:ui:home -X> :android:shared:statistics",
            ":android:mobile:ui:session -X> :android:shared:home",
            ":android:mobile:ui:session -X> :android:shared:settings",
            ":android:mobile:ui:session -X> :android:shared:statistics",
            ":android:mobile:ui:settings -X> :android:shared:home",
            ":android:mobile:ui:settings -X> :android:shared:session",
            ":android:mobile:ui:settings -X> :android:shared:statistics",
            ":android:mobile:ui:statistics -X> :android:shared:home",
            ":android:mobile:ui:statistics -X> :android:shared:session",
            ":android:mobile:ui:statistics -X> :android:shared:settings",
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
            // NO module can depend on mobile app module
            ":android:mobile:ui:.* -X> :android:mobile:app",
            ":shared-ui:.* -X> :android:mobile:app",
            ":commonUtils -X> :android:mobile:app",
            ":commonResources -X> :android:mobile:app",
            ":android:shared:core -X> :android:mobile:app",
            ":testUtils -X> :android:mobile:app",
            // commonUtils CANNOT depend on anything (foundation module)
            ":commonUtils -X> :.*",
            // testUtils CANNOT depend on anything (isolated testing)
            ":testUtils -X> :.*",
        )

    configurations = setOf("api", "implementation")
}
