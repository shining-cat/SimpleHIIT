import fr.shiningcat.simplehiit.config.SimpleHiitBuildType

plugins {
    alias(libs.plugins.simplehiit.android.application.handheld)
    alias(libs.plugins.simplehiit.koin)
    alias(libs.plugins.simplehiit.android.application.compose)
    alias(libs.plugins.kotlin.serialization)
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
    implementation(projects.models)
    implementation(projects.domain.common)
    implementation(projects.domain.home)
    implementation(projects.domain.session)
    implementation(projects.domain.settings)
    implementation(projects.domain.statistics)
    implementation(projects.sharedUi.home)
    implementation(projects.sharedUi.session)
    implementation(projects.sharedUi.settings)
    implementation(projects.sharedUi.statistics)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    implementation(projects.data)
    testImplementation(projects.testUtils)
    //
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.adaptive)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.androidx.compose)
}

moduleGraphAssert {
    maxHeight = 7

    allowed =
        arrayOf(
            // Mobile app can depend on anything (required by Android framework and DI)
            ":android:mobile:app -> .*",
            // Mobile UI feature modules - CLEAN (models only, no domain)
            ":android:mobile:ui:home -> :android:common",
            ":android:mobile:ui:home -> :android:mobile:ui:common",
            ":android:mobile:ui:home -> :shared-ui:home",
            ":android:mobile:ui:home -> :models",
            ":android:mobile:ui:home -> :commonUtils",
            ":android:mobile:ui:home -> :commonResources",
            ":android:mobile:ui:statistics -> :android:common",
            ":android:mobile:ui:statistics -> :android:mobile:ui:common",
            ":android:mobile:ui:statistics -> :shared-ui:statistics",
            ":android:mobile:ui:statistics -> :models",
            ":android:mobile:ui:statistics -> :commonUtils",
            ":android:mobile:ui:statistics -> :commonResources",
            // Mobile UI feature modules - TEMPORARY domain:common (refactoring in progress)
            ":android:mobile:ui:session -> :android:common",
            ":android:mobile:ui:session -> :android:mobile:ui:common",
            ":android:mobile:ui:session -> :shared-ui:session",
            ":android:mobile:ui:session -> :models",
            ":android:mobile:ui:session -> :domain:common",
            ":android:mobile:ui:session -> :commonUtils",
            ":android:mobile:ui:session -> :commonResources",
            ":android:mobile:ui:settings -> :android:common",
            ":android:mobile:ui:settings -> :android:mobile:ui:common",
            ":android:mobile:ui:settings -> :shared-ui:settings",
            ":android:mobile:ui:settings -> :models",
            ":android:mobile:ui:settings -> :domain:common",
            ":android:mobile:ui:settings -> :commonUtils",
            ":android:mobile:ui:settings -> :commonResources",
            // Mobile UI common module - TEMPORARY domain:common (refactoring in progress)
            ":android:mobile:ui:common -> :android:common",
            ":android:mobile:ui:common -> :models",
            ":android:mobile:ui:common -> :domain:common",
            ":android:mobile:ui:common -> :commonUtils",
            ":android:mobile:ui:common -> :commonResources",
            // shared-ui modules (KMP-ready: domain, models and utils only)
            ":shared-ui:.* -> :domain:.*",
            ":shared-ui:.* -> :models",
            ":shared-ui:.* -> :commonUtils",
            // Domain modules depend on models, domain:common, and commonUtils
            ":domain:home -> :models",
            ":domain:home -> :domain:common",
            ":domain:home -> :commonUtils",
            ":domain:session -> :models",
            ":domain:session -> :domain:common",
            ":domain:session -> :commonUtils",
            ":domain:settings -> :models",
            ":domain:settings -> :domain:common",
            ":domain:settings -> :commonUtils",
            ":domain:statistics -> :models",
            ":domain:statistics -> :domain:common",
            ":domain:statistics -> :commonUtils",
            // Domain common depends on models and commonUtils
            ":domain:common -> :models",
            ":domain:common -> :commonUtils",
            // Data depends on models, domain:common and commonUtils
            ":data -> :models",
            ":data -> :domain:common",
            ":data -> :commonUtils",
            // Common resources depends on models and domain:common
            ":commonResources -> :models",
            ":commonResources -> :domain:common",
            // Android common can depend on commonResources, domain, shared-ui, models and commonUtils
            ":android:common -> :commonResources",
            ":android:common -> :domain:.*",
            ":android:common -> :shared-ui:.*",
            ":android:common -> :models",
            ":android:common -> :commonUtils",
            // Models module is foundation - no dependencies
            ":models -> (nothing allowed)",
        )

    restricted =
        arrayOf(
            // Domain modules CANNOT depend on data layer
            ":domain:.* -X> :data",
            // Domain modules CANNOT depend on UI modules
            ":domain:.* -X> :android:mobile:ui:.*",
            ":domain:.* -X> :shared-ui:.*",
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
            // shared-ui CANNOT depend on Android-specific modules (KMP-ready)
            ":shared-ui:.* -X> :android:.*",
            // shared-ui CANNOT depend on commonResources (Android-specific)
            ":shared-ui:.* -X> :commonResources",
            // UI modules CANNOT depend on data layer
            ":android:mobile:ui:.* -X> :data",
            // NO module can depend on mobile app module
            ":android:mobile:ui:.* -X> :android:mobile:app",
            ":shared-ui:.* -X> :android:mobile:app",
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
