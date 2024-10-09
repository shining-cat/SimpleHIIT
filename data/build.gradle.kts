plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "fr.shiningcat.simplehiit.data"
}

dependencies {
    implementation(projects.domain.common)
    implementation(projects.commonUtils)
    testImplementation(projects.testUtils)
    androidTestImplementation(projects.testUtils)
    //
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    implementation(Deps.datastore)
    implementation(RoomDeps.roomRuntime)
    implementation(RoomDeps.roomCoroutinesExtensions)
    kapt(RoomDeps.roomKaptCompiler)
    //
    testImplementation(HiltDeps.hiltTestAndroid)
    testImplementation(TestDeps.coroutinesTest)
    testImplementation(TestDeps.mockk)
    testImplementation(TestDeps.jupiter)
}
