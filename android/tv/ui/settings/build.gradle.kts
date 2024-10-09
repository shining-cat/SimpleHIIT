plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "fr.shiningcat.simplehiit.android.tv.ui.settings"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinComposeCompiler.get()
    }
}

dependencies {
    implementation(projects.android.common)
    implementation(projects.android.tv.ui.common)
    implementation(projects.domain.common)
    implementation(projects.domain.settings)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
    //
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    //
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.google.material)
    implementation(libs.androidx.compose.preview)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    kapt(libs.dagger.hilt.compiler)
    //
    debugImplementation(libs.androidx.compose.preview.debug)
    debugImplementation(libs.androidx.compose.ui.test.debug)

    //
    testImplementation(libs.dagger.hilt.android.testing)
    testImplementation(libs.jetbrains.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}
