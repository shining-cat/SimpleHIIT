plugins {
    id("libraries_gradle_config")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "fr.shining_cat.simplehiit.android.mobile.ui.session"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3" // see https://developer.android.com/jetpack/androidx/releases/compose-kotlin for kotlin and compose compile version compatibility
    }

}

dependencies {
    implementation(project(":android:common"))
    implementation(project(":domain:common"))
    implementation(project(":domain:session"))
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