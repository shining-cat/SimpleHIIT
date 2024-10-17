plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    jacoco
}

android {
    namespace = "fr.shiningcat.simplehiit.domain.common"
}

dependencies {
    implementation(projects.commonUtils)
    testImplementation(projects.testUtils)
    //
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.test.runner)
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.jetbrains.coroutines.test)
    kspAndroidTest(libs.dagger.hilt.android.compiler)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
