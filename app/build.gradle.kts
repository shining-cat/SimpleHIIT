plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    jacoco
}

android {
    compileSdk = ConfigData.compileSdkVersion
    buildToolsVersion = ConfigData.buildToolsVersion

    namespace = "fr.shining_cat.simplehiit"

    defaultConfig {
        applicationId = "fr.shining_cat.simplehiit"
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName

        testInstrumentationRunner = "fr.shining_cat.simplehiit.HiltTestRunner"
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

repositories {
    google()
    mavenCentral()
}

dependencies {
    //This is to prevent the older version pulled by AGP to override the newer needed by Hilt
    //implementation("com.squareup:javapoet:1.13.0") //seems to not be needed here but only in project build.gradle and buildSrc one
    //
    val composeBom = platform("androidx.compose:compose-bom:2022.12.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    //
    implementation(Deps.kotlin)
    implementation(Deps.appCompat)
    implementation(Deps.datastore)
    implementation(Deps.materialDesign)
    implementation(Deps.constraintLayout)
    testImplementation(Deps.jupiter)
    androidTestImplementation(Deps.archCoreTesting)
    androidTestImplementation(Deps.testRunner)
    //
    implementation(HiltDeps.hiltAndroid)
    implementation(HiltDeps.hiltNavigation)
    kapt(HiltDeps.hiltAndroidCompiler)
    testImplementation(HiltDeps.hiltTestAndroid)
    kaptTest(HiltDeps.hiltAndroidTestAnnotationProcessor)
    androidTestImplementation(HiltDeps.hiltTestAndroid)
    kaptAndroidTest(HiltDeps.hiltAndroidTestAnnotationProcessor)
    //
    implementation(ComposeDeps.composeMaterial3)
    implementation(ComposeDeps.composePreview)
    debugImplementation(ComposeDeps.composePreviewDebug)
    androidTestImplementation(ComposeDeps.composeUiTests)
    debugImplementation(ComposeDeps.composeUiTestsDebug)
    implementation(ComposeDeps.composeActivities)
    implementation(ComposeDeps.composeViewModels)
    implementation(ComposeDeps.composeLiveData)
    //
    implementation(RoomDeps.roomRuntime)
    kapt(RoomDeps.roomKaptCompiler)
    implementation(RoomDeps.roomCoroutinesExtensions)
    testImplementation(RoomDeps.roomTestHelpers)
    //
    implementation(Navigation.navCompose)
    implementation(Navigation.navFragments)
    implementation(Navigation.navUi)
    testImplementation(Navigation.navTesting)
    //
    testImplementation(Deps.mockk)
    testImplementation(Deps.coroutinesTest)
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
