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
    implementation(Deps.datastore)
    implementation(RoomDeps.roomRuntime)
    implementation(RoomDeps.roomCoroutinesExtensions)
    kapt(RoomDeps.roomKaptCompiler)
    //
    implementation(HiltDeps.hiltTestAndroid)
    implementation(TestDeps.coroutinesTest)
    implementation(RoomDeps.roomTestHelpers)
    implementation(TestDeps.archCoreTesting)
    implementation(TestDeps.testRunner)
    implementation(TestDeps.junit)
    kapt(HiltDeps.hiltAndroidTestAnnotationProcessor)
}
