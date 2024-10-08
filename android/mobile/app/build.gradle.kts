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
    implementation(project(":android:common"))
    implementation(project(":android:mobile:ui:common"))
    implementation(project(":android:mobile:ui:home"))
    implementation(project(":android:mobile:ui:session"))
    implementation(project(":android:mobile:ui:settings"))
    implementation(project(":android:mobile:ui:statistics"))
    implementation(project(":domain:common"))
    implementation(project(":commonUtils"))
    implementation(project(":commonResources"))
    implementation(project(":data"))
    //
    val composeBom = platform("androidx.compose:compose-bom:${Versions.COMPOSE_BOM}")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    //
    implementation(Deps.appCompat)
    implementation(Deps.androidXLifeCycleProcess)
    implementation(HiltDeps.hiltAndroid)
    implementation(ComposeDeps.composeMaterial3)
    implementation(ComposeDeps.composeMaterial3WindowSize)
    implementation(Navigation.navCompose)
    kapt(HiltDeps.hiltAndroidCompiler)
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
