import java.util.Locale

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.7.0")
        // this has to be kept in sync with kotlinCompilerExtension version. See https://developer.android.com/jetpack/androidx/releases/compose-kotlin for kotlin and compose compile version compatibility
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.20")
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
    id("com.google.dagger.hilt.android") version Versions.HILT apply false
    // //////////////////////////////
    // Jacoco aggregation plugin by Guillermo Mazzola: https://github.com/gmazzo/gradle-android-test-aggregation-plugin
    // see below testAggregation block for included modules declaration
    // launch with: jacocoAgregatedReport (for coverage) and testAggregateReport (for results) in SimpleHIIT>Tasks>verification
    id("io.github.gmazzo.test.aggregation.coverage") version Versions.GMAZZO_JACOCO_REPORT_AGGREGATION_PLUGIN
    id("io.github.gmazzo.test.aggregation.results") version Versions.GMAZZO_JACOCO_REPORT_AGGREGATION_PLUGIN
    // //////////////////////////////
    // Dependencies' new versions detection plugin by Ben Mannes: https://github.com/ben-manes/gradle-versions-plugin
    // to launch analysis of external dependencies versions, enter in the terminal ./gradlew dependencyUpdates
    // the task is also listed in the gradle window under SimpleHIIT>Tasks>help>dependencyUpdates
    id("com.github.ben-manes.versions") version Versions.BEN_MANES_DEPENDENCIES_VERSION_PLUGIN
    // //////////////////////////////
    // Inter-modules dependencies graph generator by Savvas Dalkitsis: https://github.com/savvasdalkitsis/module-dependency-graph#module-dependency-graph
    // launch with ./gradlew graphModules or in SimpleHIIT>Tasks>other>graphModules
    id("com.savvasdalkitsis.module-dependency-graph") version Versions.SAVVASDALKITSIS_DEPENDENCY_GRAPH_PLUGIN
    // //////////////////////////////
    // KTLINT gradle plugin https://github.com/JLLeitschuh/ktlint-gradle
    id("org.jlleitschuh.gradle.ktlint") version Versions.KTLINT_GRADLE_PLUGIN
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version should be inherited from parent

    repositories {
        // Required to download KtLint
        mavenCentral()
    }

    // Optionally configure plugin
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(true)
    }
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
        // we can either explicitly declare everything (include and excludes) or simply the exclusion. The latter seems more straightforward
//        include(project(":app"))
//        include(project(":commonDomain"))
//        include(project(":commonUtils"))
//        include(project(":data"))
        exclude(project(":dataInstrumentedTests"))
    }
}

ktlint {
    // without this explicit setting, the global ktlintCheck task fails locally and on the CI with
    // Unable to load class 'com.pinterest.ktlint.rule.engine.core.api.RuleAutocorrectApproveHandler':
    version.set("1.3.1")
    android.set(true)
    outputColorName.set("RED")
    dependencies {
        ktlintRuleset("io.nlopez.compose.rules:ktlint:${Versions.KTLINT_COMPOSE_RULESET}")
    }
}
