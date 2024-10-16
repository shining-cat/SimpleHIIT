import java.util.Locale

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        // this has to be kept in sync with kotlinCompilerExtension version. See https://developer.android.com/jetpack/androidx/releases/compose-kotlin for kotlin and compose compile version compatibility
        classpath(libs.kotlin.gradle.plugin)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        // maven(url = "https://plugins.gradle.org/m2/")
    }
}

plugins {
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.gmazzo.jacoco.test.coverage)
    alias(libs.plugins.gmazzo.jacoco.test.results)
    alias(libs.plugins.dependencyupdate)
    alias(libs.plugins.dependencygraph)
    alias(libs.plugins.ktlint.gradle)
}

subprojects {
    // TODO: this subproject block should be avoided, move this config to shared gradle config files
    apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version is inherited from parent, but not if put into the children's build files

    repositories {
        // Required to download KtLint
        mavenCentral()
    }

    // Optionally configure plugin
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(true)
    }
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
        ktlintRuleset(libs.ktlint.compose.ruleset)
    }
}
