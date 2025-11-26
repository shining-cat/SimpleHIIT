plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
}

android {
    namespace = "fr.shiningcat.simplehiit.commonresources"
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(projects.domain.common)
    //
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    ignoreFailures = false
    outputs.upToDateWhen { false }
}
