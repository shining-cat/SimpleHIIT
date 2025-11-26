plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.android.library.compose)
}

android {
    namespace = "fr.shiningcat.simplehiit.android.mobile.ui.common"

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(projects.android.common)
    implementation(projects.domain.common)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
    //
    implementation(libs.google.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.preview)
    implementation(libs.androidx.lifecycle)
    //
    debugImplementation(libs.androidx.compose.preview.debug)
    debugImplementation(libs.androidx.compose.ui.test.debug)
    //
    testImplementation(libs.jetbrains.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    ignoreFailures = false
    outputs.upToDateWhen { false }
}
