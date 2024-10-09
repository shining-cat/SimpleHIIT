plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "fr.shiningcat.simplehiit.commonresources"
}

dependencies {
    implementation(projects.domain.common)
    //
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    //
    testImplementation(libs.dagger.hilt.android.testing)
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}
