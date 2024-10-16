plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.simplehiit.hilt)
}

android {
    namespace = "fr.shiningcat.simplehiit.commonresources"
}

dependencies {
    implementation(projects.domain.common)
    //

    //
    testImplementation(libs.dagger.hilt.android.testing)
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}
