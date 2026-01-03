plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.android.library.compose)
    alias(libs.plugins.simplehiit.compose.navigation)
    alias(libs.plugins.simplehiit.mobile.compose.ui)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.simplehiit.koin.compose)
    alias(libs.plugins.kover)
}

dependencies {
    implementation(projects.android.shared)
    implementation(projects.android.mobile.ui.common)
    implementation(projects.sharedUi.settings)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    implementation(projects.models)
    implementation(projects.domain.common)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
}
