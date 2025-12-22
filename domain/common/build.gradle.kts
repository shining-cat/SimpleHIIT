plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
}

dependencies {
    implementation(projects.commonUtils)
    // TEST: This violates the rule that domain modules cannot depend on data layer
    implementation(projects.data)
    testImplementation(projects.testUtils)
}
