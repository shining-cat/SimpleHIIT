plugins {
    id("libraries_gradle_config")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "fr.shining_cat.simplehiit.android.mobile.ui.common"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7" // see https://developer.android.com/jetpack/androidx/releases/compose-kotlin for kotlin and compose compile version compatibility (1.4.7 => 1.8.21)
    }

}

dependencies {
    implementation(project(":android:common"))
    implementation(project(":domain:common"))
    implementation(project(":commonUtils"))
    implementation(project(":commonResources"))
    testImplementation(project(":testUtils"))
    androidTestImplementation(project(":testUtils"))
    //
    val composeBom = platform("androidx.compose:compose-bom:${Versions.composeBom}")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    //
    implementation(HiltDeps.hiltAndroid)
    implementation(Deps.materialDesign)
    implementation(ComposeDeps.composeMaterial3)
    implementation(ComposeDeps.composePreview)
    implementation(Deps.coil)
    implementation(Deps.coilGif)
    kapt(HiltDeps.hiltAndroidCompiler)
    //
    debugImplementation(ComposeDeps.composePreviewDebug)
    debugImplementation(ComposeDeps.composeUiTestsDebug)

    //
    testImplementation(HiltDeps.hiltTestAndroid)
    testImplementation(TestDeps.coroutinesTest)
    testImplementation(TestDeps.mockk)
    testImplementation(TestDeps.jupiter)
}