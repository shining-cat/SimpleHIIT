pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // TODO: uncomment when everything is migrated to plugins
    repositories {
        google()
        mavenCentral()
    }
}

// this is to allow targeting modules from their build.gradle file through project.group.name:
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
//
rootProject.name = "SimpleHIIT"
//
include(":android:mobile:app")
include(":android:mobile:ui:common")
include(":android:mobile:ui:home")
include(":android:mobile:ui:session")
include(":android:mobile:ui:settings")
include(":android:mobile:ui:statistics")
include(":android:tv:app")
include(":android:tv:ui:common")
include(":android:tv:ui:home")
include(":android:tv:ui:session")
include(":android:tv:ui:settings")
include(":android:tv:ui:statistics")
include(":android:common")
include(":commonResources")
include(":domain:common")
include(":domain:home")
include(":domain:settings")
include(":domain:statistics")
include(":domain:session")
include(":data")
include(":dataInstrumentedTests")
include(":testUtils")
include(":commonUtils")
