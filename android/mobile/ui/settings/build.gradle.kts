plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.android.library.compose)
    jacoco
}

android {
    namespace = "fr.shiningcat.simplehiit.android.mobile.ui.settings"
}

dependencies {
    implementation(projects.android.common)
    implementation(projects.domain.common)
    implementation(projects.domain.settings)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    implementation(projects.android.mobile.ui.common)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
    //
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.google.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    //
    debugImplementation(libs.androidx.compose.preview.debug)
    debugImplementation(libs.androidx.compose.ui.test.debug)
    //
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.jetbrains.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
