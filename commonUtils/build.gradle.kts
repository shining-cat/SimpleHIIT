plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "fr.shiningcat.simplehiit.commonutils"
}

dependencies {
    // the commonUtils module is a dependency to any other, so is allowed NO dependency to ANY other module
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    //
    testImplementation(HiltDeps.hiltTestAndroid)
    testImplementation(TestDeps.testRunner)
    testImplementation(TestDeps.jupiter)
    testImplementation(TestDeps.mockk)
    testImplementation(TestDeps.coroutinesTest)
}
