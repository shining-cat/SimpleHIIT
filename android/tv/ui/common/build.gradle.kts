plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.android.library.compose)
    alias(libs.plugins.simplehiit.tv.compose.ui)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.simplehiit.koin.compose)
}

dependencies {
    implementation(projects.android.common)
    implementation(projects.domain.common)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
}
