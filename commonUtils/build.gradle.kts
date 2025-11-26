plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.kover)
}

kover {
    reports {
        filters {
            excludes {
                // Exclude by annotation (Dagger generates with @DaggerGenerated)
                annotatedBy("dagger.internal.DaggerGenerated")

                // Also try pattern like BuildConfig (which works)
                classes("*._Factory")
                classes("*._MembersInjector")
            }
        }
    }
}

android {
    namespace = "fr.shiningcat.simplehiit.commonutils"

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    // the commonUtils module is a dependency to any other, so is allowed NO dependency to ANY other module
    testImplementation(libs.test.runner)
    testImplementation(libs.test.runner)
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.jetbrains.coroutines.test)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    ignoreFailures = false
    outputs.upToDateWhen { false }
}
