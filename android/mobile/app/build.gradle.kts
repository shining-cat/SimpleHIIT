import fr.shiningcat.simplehiit.config.SimpleHiitBuildType

plugins {
    alias(libs.plugins.simplehiit.android.application.handheld)
    kotlin("kapt")
    alias(libs.plugins.simplehiit.hilt)
    jacoco
    alias(libs.plugins.simplehiit.android.application.compose)
}

android {
    buildTypes {
        debug {
            // for some reason, this parameter is not accessible in the configureBuildTypes extension, need to set this here:
            applicationIdSuffix = SimpleHiitBuildType.DEBUG.applicationIdSuffix
        }
    }
}

dependencies {
    implementation(projects.android.common)
    implementation(projects.android.mobile.ui.common)
    implementation(projects.android.mobile.ui.home)
    implementation(projects.android.mobile.ui.session)
    implementation(projects.android.mobile.ui.settings)
    implementation(projects.android.mobile.ui.statistics)
    implementation(projects.domain.common)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    implementation(projects.data)
    //
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    //
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.adaptive)
    implementation(libs.androidx.navigation.compose)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
