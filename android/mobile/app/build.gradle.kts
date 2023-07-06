plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    jacoco
}

android {
    namespace = "fr.shining_cat.simplehiit"

    compileSdk = ConfigData.compileSdkVersion
    buildToolsVersion = ConfigData.buildToolsVersion

    defaultConfig {
        applicationId = "fr.shining_cat.simplehiit"
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName

        testInstrumentationRunner = "fr.shining_cat.simplehiit.testutils.HiltTestRunner"
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
                    "META-INF/LICENSE-notice.md",
                )
            )
        }
    }

    composeOptions {
        //see https://developer.android.com/jetpack/androidx/releases/compose-compiler for released versions.
        // this is what limits the kotlin version. As of today, 1.4.4 is only compatible with kotlin 1.8.10
        kotlinCompilerExtensionVersion = "1.4.7"
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
    val composeBom = platform("androidx.compose:compose-bom:${Versions.composeBom}")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    //
    implementation(Deps.appCompat)
    implementation(HiltDeps.hiltAndroid)
    implementation(ComposeDeps.composeMaterial3)
    implementation(ComposeDeps.composeMaterial3WindowSize)
    implementation(Navigation.navCompose)
    kapt(HiltDeps.hiltAndroidCompiler)
}

//Allow references to generated code
kapt {
    correctErrorTypes = true
}

tasks {

    withType<Test> {
        useJUnitPlatform()
    }

}
