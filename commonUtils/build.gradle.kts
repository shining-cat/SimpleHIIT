plugins{
    id("libraries_gradle_config")
    id("com.google.dagger.hilt.android")//TODO: find out if we can include this in libraries_gradle_config: it causes a resolution error...
}

android {
    namespace = "fr.shining_cat.simplehiit.commonutils"
}

dependencies {
    //the commonUtils module is a dependency to any other, so is allowed NO dependency to ANY other module
    implementation(HiltDeps.hiltAndroid)
    kapt(HiltDeps.hiltAndroidCompiler)
    //
    testImplementation(HiltDeps.hiltTestAndroid)
    testImplementation(TestDeps.testRunner)
    testImplementation(TestDeps.jupiter)
    testImplementation(TestDeps.mockk)
    testImplementation(TestDeps.coroutinesTest)
}