/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.android.library.compose)
    alias(libs.plugins.simplehiit.compose.navigation)
    alias(libs.plugins.simplehiit.tv.compose.ui)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.simplehiit.koin.compose)
    alias(libs.plugins.kover)
}

dependencies {
    implementation(projects.android.shared.core)
    implementation(projects.android.shared.settings)
    implementation(projects.android.tv.ui.common)
    implementation(projects.sharedUi.settings)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    implementation(projects.models)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
}
