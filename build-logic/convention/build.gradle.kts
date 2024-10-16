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
}

gradlePlugin {
    plugins {
        register("androidHandheldApp") {
            id = "fr.shiningcat.simplehiit.android.handheld.application"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.AndroidHandheldAppConventionPlugin"
        }
        register("androidTvApp") {
            id = "fr.shiningcat.simplehiit.android.tv.application"
            implementationClass =
                "fr.shiningcat.simplehiit.plugins.AndroidTvAppConventionPlugin"
        }
    }
}
