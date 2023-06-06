plugins {
    id("libraries_gradle_config")
    id("com.google.dagger.hilt.android")//TODO: find out if we can include this in libraries_gradle_config: it causes a resolution error...
}

android {
    namespace = "fr.shining_cat.simplehiit.domain.home"
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