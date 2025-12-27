plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.android.library.compose)
    alias(libs.plugins.simplehiit.compose.navigation)
    alias(libs.plugins.simplehiit.tv.compose.ui)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
}

dependencies {
    implementation(projects.android.common)
    implementation(projects.android.tv.ui.common)
    implementation(projects.sharedUi.settings)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
}
