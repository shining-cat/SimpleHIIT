plugins {
    id("libraries_gradle_config")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "fr.shiningcat.simplehiit.testutils"
}

dependencies {
    /**
     * This whole module is here to be added as a testImplementation dependency only
     * there is no way as of now to inherit from classes defined in the test folder of a module from other modules,
     * so the only workaround is to define it in the main sourceset, adding the test dependencies needed for it as
     * "normal" dependencies (using implementation calls instead of testImplementations)
     */
    implementation(HiltDeps.hiltAndroid)
    kapt(HiltDeps.hiltAndroidCompiler)
    implementation(HiltDeps.hiltTestAndroid)
    implementation(TestDeps.jupiter)
    implementation(TestDeps.testRunner)
    implementation(TestDeps.mockk)
    //
    implementation(project(":commonUtils"))
}
