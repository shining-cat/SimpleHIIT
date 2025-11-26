plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
}

android {
    namespace = "fr.shiningcat.simplehiit.domain.common"
}

dependencies {
    implementation(projects.commonUtils)
    testImplementation(projects.testUtils)
}
