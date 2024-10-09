plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    alias(libs.plugins.dagger.hilt.android)
    jacoco
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "fr.shiningcat.simplehiit.android.tv.app"

    compileSdk = ConfigData.tvCompileSdkVersion

    defaultConfig {
        applicationId = ConfigData.applicationID
        minSdk = ConfigData.tvMinSdkVersion
        targetSdk = ConfigData.tvTargetSdkVersion
        versionCode = ConfigData.tvVersionCode
        versionName = ConfigData.tvVersionName

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

    kotlinOptions {
        jvmTarget = "17"
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(projects.android.common )
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
    implementation(Deps.appCompat)
    implementation(Deps.androidXLifeCycleProcess)
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.compose.material)
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
