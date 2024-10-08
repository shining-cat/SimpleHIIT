plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "fr.shiningcat.simplehiit.domain.common"
}

dependencies {
    implementation(project(":commonUtils"))
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
