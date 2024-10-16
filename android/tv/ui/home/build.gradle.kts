plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.android.library.compose)
}

android {
    namespace = "fr.shiningcat.simplehiit.android.tv.ui.home"
}

dependencies {
    implementation(projects.android.common)
    implementation(projects.android.tv.ui.common)
    implementation(projects.domain.common)
    implementation(projects.domain.home)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
    //
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    //

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.google.material)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.compose.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    //
    debugImplementation(libs.androidx.compose.preview.debug)
    debugImplementation(libs.androidx.compose.ui.test.debug)

    //
    testImplementation(libs.dagger.hilt.android.testing)
    testImplementation(libs.jetbrains.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}
