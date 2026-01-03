plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.koin)
    alias(libs.plugins.simplehiit.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.simplehiit.testing)
}

dependencies {
    implementation(projects.models)
    implementation(projects.domain.common)
    implementation(projects.domain.home)
    implementation(projects.domain.settings)
    implementation(projects.sharedUi.home)
    implementation(projects.sharedUi.settings)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
    //
    implementation(libs.androidx.lifecycle)
    implementation(libs.google.material)
    implementation(libs.androidx.compose.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.glide.core)
    implementation(libs.glide.compose)
    // Navigation 3 dependencies for Screen.kt NavKey interface
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.kotlinx.serialization.core)
}
