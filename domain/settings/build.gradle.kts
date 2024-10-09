plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "fr.shiningcat.simplehiit.domain.settings"
}

dependencies {
    implementation(projects.commonUtils)
    implementation(projects.domain.common)
    testImplementation(projects.testUtils)
    //
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    //
    testImplementation(libs.dagger.hilt.android.testing)
    testImplementation(libs.test.runner)
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.jetbrains.coroutines.test)
    kaptAndroidTest(libs.dagger.hilt.android.compiler)
}
