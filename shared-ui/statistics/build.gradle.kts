plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
}

dependencies {
    implementation(projects.sharedUi.common)
    implementation(projects.commonUtils)
    implementation(projects.domain.common)
    implementation(projects.domain.statistics)
    testImplementation(projects.testUtils)
}
