plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.simplehiit.hilt)
}

android {
    namespace = "fr.shiningcat.simplehiit.domain.home"
}

dependencies {
    implementation(projects.commonUtils)
    implementation(projects.domain.common)
    testImplementation(projects.testUtils)
    //

    //
    testImplementation(libs.dagger.hilt.android.testing)
    testImplementation(libs.test.runner)
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.jetbrains.coroutines.test)
    kaptAndroidTest(libs.dagger.hilt.android.compiler)
}
