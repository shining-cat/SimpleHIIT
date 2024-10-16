plugins {
    kotlin("kapt")
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    jacoco
}

android {
    namespace = "fr.shiningcat.simplehiit.commonutils"
}

dependencies {
    // the commonUtils module is a dependency to any other, so is allowed NO dependency to ANY other module
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.test.runner)
    testImplementation(libs.test.runner)
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.jetbrains.coroutines.test)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
