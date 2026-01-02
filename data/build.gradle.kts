plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.koin) // Add Koin for future migration
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
}

dependencies {
    implementation(projects.models)
    implementation(projects.domain.common)
    implementation(projects.commonUtils)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
    //
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.coroutines)
    ksp(libs.androidx.room.compiler)
}
