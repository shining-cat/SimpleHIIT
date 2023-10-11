plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    jacoco
}

android {
    namespace = "fr.shiningcat.simplehiit.android.tv.app"

    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        applicationId = "fr.shiningcat.simplehiit"
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName

        testInstrumentationRunner = "fr.shiningcat.simplehiit.testutils.HiltTestRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
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
                    "META-INF/LICENSE-notice.md"
                )
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.kotlinCompilerExtension
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
    val composeBom = platform("androidx.compose:compose-bom:${Versions.composeBom}")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    //
    implementation(Deps.appCompat)
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
