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
        kotlinCompilerExtensionVersion = Versions.KOTLIN_COMPILER_EXTENSION
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
    implementation(Deps.materialDesign)
    implementation(libs.androidx.compose.preview)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.compose.material)
    implementation(libs.androidx.navigation.compose)
    implementation(Deps.androidXLifeCycleProcess)
    implementation(Deps.coil)
    implementation(Deps.coilGif)
    kapt(libs.dagger.hilt.compiler)
    //
    debugImplementation(libs.androidx.compose.preview.debug)
    debugImplementation(libs.androidx.compose.ui.test.debug)

    //
    testImplementation(HiltDeps.hiltTestAndroid)
    testImplementation(TestDeps.coroutinesTest)
    testImplementation(TestDeps.mockk)
    testImplementation(TestDeps.jupiter)
}
