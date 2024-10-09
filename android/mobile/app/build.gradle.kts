plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    alias(libs.plugins.dagger.hilt.android)
    jacoco
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "fr.shiningcat.simplehiit.android.mobile.app"

    compileSdk = ConfigData.handheldCompileSdkVersion

    defaultConfig {
        applicationId = ConfigData.applicationID
        minSdk = ConfigData.handheldMinSdkVersion
        targetSdk = ConfigData.handheldTargetSdkVersion
        versionCode = ConfigData.handheldVersionCode
        versionName = ConfigData.handheldVersionName

        testInstrumentationRunner = "fr.shiningcat.simplehiit.testutils.HiltTestRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
            enableUnitTestCoverage = true
        }
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes.addAll(
                listOf(
                    "META-INF/LICENSE.md",
                    "META-INF/LICENSE-notice.md",
                ),
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.KOTLIN_COMPILER_EXTENSION
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

repositories {
    google()
    mavenCentral()
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
    implementation(Deps.appCompat)
    implementation(Deps.androidXLifeCycleProcess)
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.adaptive)
    implementation(libs.androidx.navigation.compose)
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
