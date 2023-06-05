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
    implementation(project(":commonDomain"))
    implementation(project(":commonUtils"))
    implementation(project(":commonResources"))
    implementation(project(":android:mobile:ui:common"))
    implementation(project(":android:mobile:ui:home"))
    implementation(project(":android:mobile:ui:session"))
    implementation(project(":android:mobile:ui:settings"))
    implementation(project(":android:mobile:ui:statistics"))
    implementation(project(":data"))
    testImplementation(project(":testUtils"))
    androidTestImplementation(project(":testUtils"))
    //
    val composeBom = platform("androidx.compose:compose-bom:${Versions.composeBom}")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    //
    implementation(Deps.appCompat)
    implementation(Deps.datastore)
    implementation(Deps.materialDesign)
    implementation(HiltDeps.hiltAndroid)
    implementation(HiltDeps.hiltNavigation)
    implementation(ComposeDeps.composeMaterial3)
    implementation(ComposeDeps.composePreview)
    implementation(Navigation.navCompose)
    implementation(Navigation.navFragments)
    implementation(Navigation.navUi)
    implementation(Deps.coil)
    implementation(Deps.coilGif)
    kapt(HiltDeps.hiltAndroidCompiler)
    //
    debugImplementation(ComposeDeps.composePreviewDebug)
    debugImplementation(ComposeDeps.composeUiTestsDebug)
    //
    testImplementation(HiltDeps.hiltTestAndroid)
    testImplementation(Navigation.navTesting)
    testImplementation(TestDeps.mockk)
    testImplementation(TestDeps.coroutinesTest)
    testImplementation(TestDeps.jupiter)
    //
    androidTestImplementation(HiltDeps.hiltTestAndroid)
    androidTestImplementation(TestDeps.archCoreTesting)
    androidTestImplementation(ComposeDeps.composeUiTests)
    androidTestImplementation(TestDeps.testRunner)
    kaptAndroidTest(HiltDeps.hiltAndroidTestAnnotationProcessor)
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
