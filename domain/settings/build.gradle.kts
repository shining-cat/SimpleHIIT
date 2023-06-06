plugins {
    id("libraries_gradle_config")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "fr.shining_cat.simplehiit.domain.settings"
}

dependencies {
    implementation(project(":commonUtils"))
    implementation(project(":domain:common"))
    testImplementation(project(":testUtils"))
    //
    implementation(HiltDeps.hiltAndroid)
    kapt(HiltDeps.hiltAndroidCompiler)
    //
    testImplementation(HiltDeps.hiltTestAndroid)
    testImplementation(TestDeps.testRunner)
    testImplementation(TestDeps.jupiter)
    testImplementation(TestDeps.mockk)
    testImplementation(TestDeps.coroutinesTest)
    kaptAndroidTest(HiltDeps.hiltAndroidTestAnnotationProcessor)
}