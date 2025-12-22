plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
}

dependencies {
    implementation(projects.commonUtils)
    // TEST: Adding forbidden dependency - domain modules cannot depend on data layer
    implementation(projects.data)
    testImplementation(projects.testUtils)
}
