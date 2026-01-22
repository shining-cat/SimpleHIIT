/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.koin) // Add Koin for future migration
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
}

// Export Room schemas for migration testing
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    sourceSets {
        // Include Room schema directory in androidTest assets
        getByName("androidTest") {
            assets.srcDirs("$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(projects.models)
    implementation(projects.domain.common)
    implementation(projects.commonUtils)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
    //
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.coroutines)
    ksp(libs.androidx.room.compiler)

    // Room migration instrumented testing
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.test.ext.junit)
}
