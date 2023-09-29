import java.util.Locale

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10") // this has to be kept in sync with kotlinCompilerExtension version. See https://developer.android.com/jetpack/androidx/releases/compose-kotlin for kotlin and compose compile version compatibility
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
    }
}

plugins {
    id("com.google.dagger.hilt.android") version Versions.hilt apply false
    ////////////////////////////////
    // Jacoco aggregation plugin by Guillermo Mazzola: https://github.com/gmazzo/gradle-android-test-aggregation-plugin
    // see below testAggregation block for included modules declaration
    // launch with: jacocoTestReport (for coverage) and testAggregateTestReport (for results) in SimpleHIIT>Tasks>verification
    id("io.github.gmazzo.test.aggregation.coverage") version Versions.gmazzoJacocoReportAggregationPlugin
    id("io.github.gmazzo.test.aggregation.results") version Versions.gmazzoJacocoReportAggregationPlugin
    ////////////////////////////////
    // Dependencies' new versions detection plugin by Ben Mannes: https://github.com/ben-manes/gradle-versions-plugin
    // to launch analysis of external dependencies versions, enter in the terminal ./gradlew dependencyUpdates
    // the task is also listed in the gradle window under SimpleHIIT>Tasks>help>dependencyUpdates
    id("com.github.ben-manes.versions") version Versions.benManesDependenciesVersionPlugin
    ////////////////////////////////
    // Inter-modules dependencies graph generator by Savvas Dalkitsis: https://github.com/savvasdalkitsis/module-dependency-graph#module-dependency-graph
    // launch with ./gradlew graphModules or in SimpleHIIT>Tasks>other>graphModules
    id("com.savvasdalkitsis.module-dependency-graph") version Versions.savvasdalkitsisDependencyGraphPlugin
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