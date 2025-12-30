plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.koin)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
}

dependencies {
    implementation(projects.commonUtils)
    implementation(projects.domain.common)
    implementation(projects.domain.home)
    testImplementation(projects.testUtils)
}
