plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.android.library.compose)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
}

android {
    namespace = "fr.shiningcat.simplehiit.android.mobile.ui.statistics"
}

dependencies {
    implementation(projects.android.common)
    implementation(projects.domain.common)
    implementation(projects.domain.statistics)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    implementation(projects.android.mobile.ui.common)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
    //
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.google.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.adaptive)
    implementation(libs.androidx.compose.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle)
    //
    debugImplementation(libs.androidx.compose.preview.debug)
    debugImplementation(libs.androidx.compose.ui.test.debug)
}
