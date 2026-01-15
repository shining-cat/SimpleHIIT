/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.koin)
    alias(libs.plugins.simplehiit.android.library.compose)
    alias(libs.plugins.simplehiit.compose.navigation)
    alias(libs.plugins.simplehiit.testing)
}

dependencies {
    implementation(projects.models)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)

    // Exception: Module-specific dependencies for GIF components
    implementation(libs.glide.core)
    implementation(libs.glide.compose)
    // Material3 provides Foundation transitively but is not included in this module, so we need it here
    implementation(libs.androidx.compose.foundation) // For Image in GifFirstFrameStatic
}
