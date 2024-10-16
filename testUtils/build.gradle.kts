plugins {
    id("libraries_gradle_config")
    alias(libs.plugins.simplehiit.hilt)
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

    implementation(libs.hilt.android.testing)
    implementation(libs.jupiter)
    implementation(libs.test.runner)
    implementation(libs.mockk)
    //
    implementation(project(":commonUtils"))
}
