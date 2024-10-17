import java.util.Locale

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint.gradle)
    alias(libs.plugins.gmazzo.jacoco.test.coverage)
    alias(libs.plugins.gmazzo.jacoco.test.results)
    alias(libs.plugins.dependencyupdate)
    alias(libs.plugins.dependencygraph)
}

// This is for the dependency update plugin to define which versions we're interested in:
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

// Jacoco coverage report aggregation setting:
testAggregation {
    modules {
        // we can either explicitly declare everything (include and excludes) or simply the exclusion.
        // The latter is more straightforward:
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
        // applying additional Compose ruleset:
        ktlintRuleset(libs.ktlint.compose.ruleset)
    }
}
