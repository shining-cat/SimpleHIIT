import java.util.Locale

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

}

plugins {
    id("com.google.dagger.hilt.android") version Versions.hilt apply false
    ////////////////////////////////
    // Jacoco aggregation plugin by Gmazzo: https://github.com/gmazzo/gradle-android-test-aggregation-plugin
    // see below testAggregation block for included modules declaration
    // launch with: jacocoTestReport (for coverage) and testAggregateTestReport (for results) in SimpleHIIT>Tasks>verification
    //TODO: deactivated the plugin when adding the external instrumented tests module, make this work again! see also testAggregation below
    id("io.github.gmazzo.test.aggregation.coverage") version Versions.gmazzoJacocoReportAggregationPlugin
    id("io.github.gmazzo.test.aggregation.results") version Versions.gmazzoJacocoReportAggregationPlugin
    ////////////////////////////////
    // Dependencies' new versions detection plugin by Ben Mannes: https://github.com/ben-manes/gradle-versions-plugin
    // to launch analysis of external dependencies versions, enter in the terminal ./gradlew dependencyUpdates
    // the task is also listed in the gradle window under SimpleHIIT>Tasks>help>dependencyUpdates
    id("com.github.ben-manes.versions") version Versions.benManesDependenciesVersionPlugin
}

fun String.isNonStable(): Boolean {
    val stableKeyword =
        listOf("RELEASE", "FINAL", "GA").any { uppercase(Locale.getDefault()).contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return isStable.not()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        candidate.version.isNonStable()
    }
}

testAggregation {
    modules {
        //we can either explicitly declare everything (include and excludes) or simply the exclusion. The latter seems more straightforward
//        include(project(":app"))
//        include(project(":commonDomain"))
//        include(project(":commonUtils"))
//        include(project(":data"))
        exclude(project(":dataInstrumentedTests"))
    }
}