/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
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
    alias(libs.plugins.spotless)
    alias(libs.plugins.kover)
    alias(libs.plugins.dependencyupdate)
    alias(libs.plugins.simplehiit.documentation)
}

// Configure Java Toolchain for all projects (platform-agnostic)
subprojects {
    pluginManager.withPlugin("org.jetbrains.kotlin.android") {
        extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension> {

            jvmToolchain(17)
        }
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
            jvmToolchain(17)
        }
    }
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

        // Exclude Room database layer - framework code and data classes
        // DAOs: Abstract methods with Room annotations (no business logic)
        // Database: Room database declaration (framework boilerplate)
        // Entities: Data classes with no logic
        packages("*.data.local.database.dao")
        packages("*.data.local.database.entities")
        classes("*SimpleHiitDatabase")
        // NOTE: Migrations are NOT excluded - see docs/KOVER_CODE_COVERAGE.md

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
    kover(project(":shared-ui:home"))
    kover(project(":shared-ui:session"))
    kover(project(":shared-ui:settings"))
    kover(project(":shared-ui:statistics"))
}

ktlint {
    android.set(true)
    outputColorName.set("RED")

    filter {
        exclude { element -> element.file.path.contains("generated/") }
        exclude { element -> element.file.path.contains("/build/") }
    }

    dependencies {
        // no necessary additional ruleset needed as of 2025.08
    }
}

// Spotless configuration for license headers and code formatting
spotless {
    format("markdown") {
        target("**/*.md")
        targetExclude("**/build/**", "**/.gradle/**", "_WIP-plans/**")

        // Note: License headers for markdown are managed manually due to:
        // 1. README.md has special formatting (badges, images) that automated
        //    header insertion would disrupt
        // 2. Delimiter patterns are unreliable for diverse markdown structures
        // 3. Existing docs already have headers; new docs should add manually
        //
        // Manual license header for markdown files:
        // <!--
        //   ~ SPDX-FileCopyrightText: 2024-2026 shining-cat
        //   ~ SPDX-License-Identifier: GPL-3.0-or-later
        //   -->

        trimTrailingWhitespace()
        endWithNewline()
    }

    format("misc") {
        target(".gitignore")
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**", "**/.gradle/**", "**/generated/**")

        licenseHeaderFile(
            rootProject.file("license-header.txt"),
            "(@file|package|import)",
        )
    }

    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**", "**/.gradle/**")

        licenseHeaderFile(
            rootProject.file("license-header.txt"),
            "(//|@file|import|plugins|buildscript|dependencyResolutionManagement|rootProject)",
        )
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
