plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.testing)
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

dependencies {
    // the commonUtils module is a dependency to any other, so is allowed NO dependency to ANY other module
}
