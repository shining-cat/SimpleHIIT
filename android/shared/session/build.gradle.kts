plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.koin)
    alias(libs.plugins.simplehiit.testing)
}

dependencies {
    implementation(projects.commonUtils)
    implementation(projects.sharedUi.session)
}
