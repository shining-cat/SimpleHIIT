import fr.shiningcat.simplehiit.config.SimpleHiitBuildType

plugins {
    alias(libs.plugins.simplehiit.android.application.tv)
    kotlin("kapt")
    alias(libs.plugins.dagger.hilt.android)
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
    implementation(projects.android.tv.ui.common)
    implementation(projects.android.tv.ui.home)
    implementation(projects.android.tv.ui.settings)
    implementation(projects.android.tv.ui.session)
    implementation(projects.android.tv.ui.statistics)
    implementation(projects.domain.common)
    implementation(projects.commonUtils)
    implementation(projects.commonResources)
    implementation(projects.data)
    //
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    //
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    kapt(libs.dagger.hilt.compiler)
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
