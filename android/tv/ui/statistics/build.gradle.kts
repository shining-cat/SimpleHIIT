plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "fr.shiningcat.simplehiit.android.tv.ui.statistics"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.KOTLIN_COMPILER_EXTENSION
    }
}

dependencies {
    implementation(project(":android:common"))
    implementation(project(":android:tv:ui:common"))
    implementation(project(":domain:common"))
    implementation(project(":domain:statistics"))
    implementation(project(":commonUtils"))
    implementation(project(":commonResources"))
    testImplementation(project(":testUtils"))
    androidTestImplementation(project(":testUtils"))
    //
    val composeBom = platform("androidx.compose:compose-bom:${Versions.COMPOSE_BOM}")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    //
    implementation(HiltDeps.hiltAndroid)
    implementation(HiltDeps.hiltNavigation)
    implementation(Deps.materialDesign)
    implementation(ComposeDeps.composeTVFoundation)
    implementation(ComposeDeps.composeTVMaterial3)
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
