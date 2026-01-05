plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.koin)
    alias(libs.plugins.simplehiit.testing)
}

dependencies {
    implementation(projects.domain.statistics)
    implementation(projects.sharedUi.statistics)
    implementation(projects.models)
    implementation(projects.commonUtils)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
}
