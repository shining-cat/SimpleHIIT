plugins {
    id("libraries_gradle_config")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "fr.shiningcat.simplehiit.android.mobile.ui.statistics"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.kotlinCompilerExtension
    }
}

dependencies {
    implementation(project(":android:common"))
    implementation(project(":domain:common"))
    implementation(project(":domain:statistics"))
    implementation(project(":commonUtils"))
    implementation(project(":commonResources"))
    implementation(project(":android:mobile:ui:common"))
    testImplementation(project(":testUtils"))
    androidTestImplementation(project(":testUtils"))
    //
    val composeBom = platform("androidx.compose:compose-bom:${Versions.composeBom}")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    //
    implementation(HiltDeps.hiltAndroid)
    implementation(HiltDeps.hiltNavigation)
    implementation(Deps.materialDesign)
    implementation(ComposeDeps.composeMaterial3)
    implementation(ComposeDeps.composePreview)
    implementation(Navigation.navCompose)
    implementation(Deps.androidXLifeCycleProcess)
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
