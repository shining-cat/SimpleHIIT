plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.android.library.compose)
    alias(libs.plugins.simplehiit.tv.compose.ui)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.simplehiit.koin.compose)
}

dependencies {
    implementation(projects.android.shared.core)
    implementation(projects.commonResources)
    implementation(projects.commonUtils)
    implementation(projects.models)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
}
