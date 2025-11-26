import fr.shiningcat.simplehiit.config.SimpleHiitBuildType

plugins {
    alias(libs.plugins.simplehiit.android.application.handheld)
    alias(libs.plugins.simplehiit.hilt)
    alias(libs.plugins.simplehiit.android.application.compose)
    alias(libs.plugins.simplehiit.testing)
    alias(libs.plugins.kover)
    alias(libs.plugins.modules.graph.assert)
}

android {
    buildTypes {
        debug {
            // for some reason, this parameter is not accessible in the configureBuildTypes extension, need to set this here:
            applicationIdSuffix = SimpleHiitBuildType.DEBUG.applicationIdSuffix
        }
    }
}

dependencies {
    implementation(projects.android.common)
    implementation(projects.android.mobile.ui.common)
    implementation(projects.android.mobile.ui.home)
    implementation(projects.android.mobile.ui.session)
    implementation(projects.android.mobile.ui.settings)
    implementation(projects.android.mobile.ui.statistics)
    implementation(projects.domain.common)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    implementation(projects.data)
    testImplementation(projects.testUtils)
    //
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.adaptive)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.compose)
}

moduleGraphAssert {
    maxHeight = 7

    allowed =
        arrayOf(
            // Mobile app can depend on anything (required by Android framework and DI)
            ":android:mobile:app -> .*",
            // Mobile UI modules can depend on domain, common utilities, resources, and their common UI module
            ":android:mobile:ui:.* -> :domain:.*",
            ":android:mobile:ui:.* -> :commonUtils",
            ":android:mobile:ui:.* -> :commonResources",
            ":android:mobile:ui:.* -> :android:common",
            ":android:mobile:ui:.* -> :android:mobile:ui:common",
            // Domain modules (except domain:common) can only depend on domain:common and commonUtils
            ":domain:home -> :domain:common",
            ":domain:home -> :commonUtils",
            ":domain:session -> :domain:common",
            ":domain:session -> :commonUtils",
            ":domain:settings -> :domain:common",
            ":domain:settings -> :commonUtils",
            ":domain:statistics -> :domain:common",
            ":domain:statistics -> :commonUtils",
            // Domain common can only depend on commonUtils
            ":domain:common -> :commonUtils",
            // Data can only depend on domain:common and commonUtils
            ":data -> :domain:common",
            ":data -> :commonUtils",
            // Common resources can depend on domain:common
            ":commonResources -> :domain:common",
            // Android common can depend on commonResources, domain:common, and commonUtils
            ":android:common -> :commonResources",
            ":android:common -> :domain:common",
            ":android:common -> :commonUtils",
        )

    restricted =
        arrayOf(
            // Domain modules CANNOT depend on data layer
            ":domain:.* -X> :data",
            // Domain modules CANNOT depend on UI modules
            ":domain:.* -X> :android:mobile:ui:.*",
            // Domain modules CANNOT depend on app modules
            ":domain:.* -X> :android:mobile:app",
            // Domain modules CANNOT depend on other domain modules (except domain:common via allowed rules)
            ":domain:home -X> :domain:session",
            ":domain:home -X> :domain:settings",
            ":domain:home -X> :domain:statistics",
            ":domain:session -X> :domain:home",
            ":domain:session -X> :domain:settings",
            ":domain:session -X> :domain:statistics",
            ":domain:settings -X> :domain:home",
            ":domain:settings -X> :domain:session",
            ":domain:settings -X> :domain:statistics",
            ":domain:statistics -X> :domain:home",
            ":domain:statistics -X> :domain:session",
            ":domain:statistics -X> :domain:settings",
            // Data CANNOT depend on UI modules
            ":data -X> :android:mobile:ui:.*",
            // Data CANNOT depend on app modules
            ":data -X> :android:mobile:app",
            // Data CANNOT depend on other domain modules (only domain:common via allowed rules)
            ":data -X> :domain:home",
            ":data -X> :domain:session",
            ":data -X> :domain:settings",
            ":data -X> :domain:statistics",
            // NO module can depend on mobile app module
            ":android:mobile:ui:.* -X> :android:mobile:app",
            ":commonUtils -X> :android:mobile:app",
            ":commonResources -X> :android:mobile:app",
            ":android:common -X> :android:mobile:app",
            ":testUtils -X> :android:mobile:app",
            // commonUtils CANNOT depend on anything (foundation module)
            ":commonUtils -X> :.*",
            // testUtils CANNOT depend on anything (isolated testing)
            ":testUtils -X> :.*",
        )

    configurations = setOf("api", "implementation")
}
