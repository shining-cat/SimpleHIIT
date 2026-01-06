plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.koin)
    alias(libs.plugins.simplehiit.testing)
}

dependencies {
    implementation(projects.commonUtils)
    implementation(projects.models)
    implementation(projects.sharedUi.statistics)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
}
