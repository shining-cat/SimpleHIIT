plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "fr.shiningcat.simplehiit.domain.statistics"
}

dependencies {
    implementation(projects.commonUtils)
    implementation(projects.domain.common)
    testImplementation(projects.testUtils)
    //
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    //
    testImplementation(HiltDeps.hiltTestAndroid)
    testImplementation(TestDeps.testRunner)
    testImplementation(TestDeps.jupiter)
    testImplementation(TestDeps.mockk)
    testImplementation(TestDeps.coroutinesTest)
    kaptAndroidTest(HiltDeps.hiltAndroidTestAnnotationProcessor)
}
