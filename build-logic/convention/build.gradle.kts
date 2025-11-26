import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "fr.shiningcat.simplehiit.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.kover.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidAppHandheld") {
            id = "fr.shiningcat.simplehiit.android.application.handheld"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.AndroidAppHandheldConventionPlugin"
        }
        register("androidAppTv") {
            id = "fr.shiningcat.simplehiit.android.application.tv"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.AndroidAppTvConventionPlugin"
        }
        register("androidLibrary") {
            id = "fr.shiningcat.simplehiit.android.library"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.AndroidLibraryConventionPlugin"
        }
        register("hilt") {
            id = "fr.shiningcat.simplehiit.hilt"
            implementationClass = "fr.shiningcat.simplehiit.plugins.HiltConventionPlugin"
        }
        register("androidAppCompose") {
            id = "fr.shiningcat.simplehiit.android.application.compose"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibCompose") {
            id = "fr.shiningcat.simplehiit.android.library.compose"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.AndroidLibraryComposeConventionPlugin"
        }
        register("documentation") {
            id = "fr.shiningcat.simplehiit.documentation"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.DocumentationConventionPlugin"
        }
        register("testing") {
            id = "fr.shiningcat.simplehiit.testing"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.TestingConventionPlugin"
        }
    }
}
