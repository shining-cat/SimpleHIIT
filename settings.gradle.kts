@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("build-logic")
}

plugins {
    // NOTE: Version catalog (libs) is not available in settings.gradle.kts
    id("com.gradle.develocity") version "4.3"
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        termsOfUseAgree = "yes"

        // Publish scans on build failures for debugging
        // Local developers: Use --scan flag to generate scans on demand
        // CI: Automatically publishes when builds fail
        publishing.onlyIf { it.buildResult.failures.isNotEmpty() }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
include(":android:shared:core")
include(":android:shared:home")
include(":android:shared:session")
include(":android:shared:settings")
include(":android:shared:statistics")
include(":shared-ui:home")
include(":shared-ui:session")
include(":shared-ui:settings")
include(":shared-ui:statistics")
include(":commonResources")
include(":models")
include(":domain:common")
include(":domain:home")
include(":domain:settings")
include(":domain:statistics")
include(":domain:session")
include(":data")
include(":testUtils")
include(":commonUtils")
