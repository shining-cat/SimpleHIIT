plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.android.library.compose)
    alias(libs.plugins.simplehiit.compose.navigation)
    alias(libs.plugins.simplehiit.mobile.compose.ui)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
}

dependencies {
    implementation(projects.android.common)
    implementation(projects.android.mobile.ui.common)
    implementation(projects.sharedUi.home)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
}
