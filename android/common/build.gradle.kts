plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.android.library.compose)
}

android {
    namespace = "fr.shiningcat.simplehiit.android.common"
}

dependencies {
    implementation(projects.domain.common)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
    //
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    //

    implementation(libs.androidx.lifecycle)
    implementation(libs.google.material)
    implementation(libs.androidx.lifecycle)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    //
    testImplementation(libs.dagger.hilt.android.testing)
    testImplementation(libs.jetbrains.coroutines.test)
    testImplementation(libs.jetbrains.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}
