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
    // Gradle core plugin that provides standard lifecycle tasks to any project:
    base
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint.gradle)
    alias(libs.plugins.kover)
    alias(libs.plugins.dependencyupdate)
    alias(libs.plugins.simplehiit.documentation)
}

// Configure root clean task to delete Kover reports
tasks.named<Delete>("clean") {
    delete("build/reports/kover")
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
    // Only check for stable release versions (not RC, beta, alpha, etc.)
    rejectVersionIf {
        candidate.version.isNonStable()
    }
}

// Kover configuration for multi-module coverage
// Shared exclusion configuration for both root and subproject reports
fun kotlinx.kover.gradle.plugin.dsl.KoverReportFiltersConfig.applyCommonExclusions() {
    excludes {
        // Exclude classes annotated with generated code annotations
        annotatedBy("*Generated")
        annotatedBy("javax.annotation.processing.Generated")
        annotatedBy("javax.annotation.Generated")

        // Exclude Room generated classes
        classes("*.*_Impl")

        // Exclude BuildConfig and R classes
        classes("*.BuildConfig", "*.R", "*.R$*")

        // Exclude DataBinding generated classes
        classes("*.databinding.*", "*.DataBindingInfo")

        // Exclude DI module packages (at any nesting level)
        packages("*.di")

        // Exclude all models packages across all modules
        packages("*.models")
        classes("*DTO")

        // Exclude Compose-generated classes
        classes("*PreviewParameterProvider")
        classes("*ComposableSingletons*")
    }
}

// Configure Kover for root project (aggregated reports)
kover {
    currentProject {
        instrumentation {
            disabledForTestTasks.add("testReleaseUnitTest")
        }
    }

    reports {
        filters {
            applyCommonExclusions()
        }
    }
}

// Apply same configuration to all subprojects (individual module reports)
subprojects {
    pluginManager.withPlugin("org.jetbrains.kotlinx.kover") {
        extensions.configure<kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension> {
            currentProject {
                instrumentation {
                    disabledForTestTasks.add("testReleaseUnitTest")
                }
            }

            reports {
                filters {
                    applyCommonExclusions()
                }
            }
        }
    }
}

dependencies {
    // Kover aggregates coverage from all modules that have the kover plugin applied
    kover(project(":android:mobile:app"))
    kover(project(":android:mobile:ui:home"))
    kover(project(":android:mobile:ui:session"))
    kover(project(":android:mobile:ui:settings"))
    kover(project(":android:mobile:ui:statistics"))
    kover(project(":android:tv:app"))
    kover(project(":android:tv:ui:home"))
    kover(project(":android:tv:ui:session"))
    kover(project(":android:tv:ui:settings"))
    kover(project(":android:tv:ui:statistics"))
    kover(project(":commonUtils"))
    kover(project(":data"))
    kover(project(":domain:common"))
    kover(project(":domain:home"))
    kover(project(":domain:settings"))
    kover(project(":domain:statistics"))
    kover(project(":domain:session"))
}

ktlint {
    android.set(true)
    outputColorName.set("RED")
    dependencies {
        // no necessary additional ruleset needed as of 2025.08
    }
}

// Configure all test tasks to emit test events for IDE integration
subprojects {
    tasks.withType<Test> {
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = false
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }
    }
}
