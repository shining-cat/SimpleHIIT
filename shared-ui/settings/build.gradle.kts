plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
}

android {
    namespace = "fr.shiningcat.simplehiit.sharedui.settings"
}

dependencies {
    implementation(projects.sharedUi.common)
    implementation(projects.commonUtils)
    implementation(projects.domain.common)
    implementation(projects.domain.settings)
    testImplementation(projects.testUtils)
}
