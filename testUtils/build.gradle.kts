/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.koin)
}

/*
 * This whole module is here to be added as a testImplementation dependency only
 * there is no way as of now to inherit from classes defined in the test folder of a module from other modules,
 * so the only workaround is to define it in the main sourceset, adding the test dependencies needed for it as
 * "normal" dependencies (using implementation calls instead of testImplementations)
 */

dependencies {
    implementation(libs.koin.test)
    implementation(libs.koin.android)
    implementation(libs.jupiter)
    implementation(libs.androidx.test.runner)
    implementation(libs.mockk)
    //
    implementation(project(":commonUtils"))
}
