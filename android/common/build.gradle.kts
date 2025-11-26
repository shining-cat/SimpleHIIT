plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.android.library.compose)
}

android {
    namespace = "fr.shiningcat.simplehiit.android.common"

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(projects.domain.common)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
    //
    implementation(libs.androidx.lifecycle)
    implementation(libs.google.material)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.compose.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.glide)
    //
    testImplementation(libs.jetbrains.coroutines.test)
    testImplementation(libs.jetbrains.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    ignoreFailures = false
    outputs.upToDateWhen { false }
}
