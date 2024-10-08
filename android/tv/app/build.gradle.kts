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
    implementation(project(":android:common"))
    implementation(project(":android:tv:ui:common"))
    implementation(project(":android:tv:ui:home"))
    implementation(project(":android:tv:ui:settings"))
    implementation(project(":android:tv:ui:session"))
    implementation(project(":android:tv:ui:statistics"))
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
    implementation(Navigation.navCompose)
    implementation(ComposeDeps.composeTVFoundation)
    implementation(ComposeDeps.composeTVMaterial3)
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
