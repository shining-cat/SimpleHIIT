plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.kover)
}

android {
    namespace = "fr.shiningcat.simplehiit.domain.home"

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(projects.commonUtils)
    implementation(projects.domain.common)
    testImplementation(projects.testUtils)
    //
    testImplementation(libs.test.runner)
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.jetbrains.coroutines.test)
    kspAndroidTest(libs.dagger.hilt.android.compiler)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    // Configure test task to return after execution for IDE integration
    ignoreFailures = false

    // Ensure test results are always generated
    outputs.upToDateWhen { false }
}
