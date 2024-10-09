plugins {
    id("test_modules_gradle_config")
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "fr.shiningcat.simplehiit.data"
}

dependencies {
    implementation(projects.domain.common)
    implementation(projects.commonUtils)
    implementation(projects.testUtils)
    implementation(projects.data)
    //
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.coroutines)
    kapt(libs.androidx.room.compiler)
    //
    implementation(libs.dagger.hilt.android.testing)
    implementation(libs.jetbrains.coroutines.test)
    implementation(libs.androidx.room.testing)
    implementation(libs.androidx.archcore.testing)
    implementation(libs.test.runner)
    implementation(libs.junit)
    kapt(libs.dagger.hilt.android.compiler)
}
