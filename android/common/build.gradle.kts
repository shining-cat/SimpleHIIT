plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.android.library.compose)
    jacoco
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
    implementation(libs.androidx.lifecycle)
    implementation(libs.google.material)
    implementation(libs.androidx.lifecycle)
    implementation(libs.glide)
    //
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.jetbrains.coroutines.test)
    testImplementation(libs.jetbrains.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
