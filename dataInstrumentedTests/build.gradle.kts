plugins {
    id("test_modules_gradle_config")
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "fr.shiningcat.simplehiit.data"
}

dependencies {
    implementation(project(":domain:common"))
    implementation(project(":commonUtils"))
    implementation(project(":testUtils"))
    implementation(project(":data"))
    //
    implementation(HiltDeps.hiltAndroid)
    kapt(HiltDeps.hiltAndroidCompiler)
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
