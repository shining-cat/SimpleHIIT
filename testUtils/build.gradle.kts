plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    jacoco
}

android {
    namespace = "fr.shining_cat.simplehiit.testutils"

    compileSdk = ConfigData.compileSdkVersion
    buildToolsVersion = ConfigData.buildToolsVersion

    defaultConfig {
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion

        testInstrumentationRunner = "fr.shining_cat.simplehiit.HiltTestRunner"
        consumerProguardFiles("consumer-rules.pro")
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
            enableUnitTestCoverage = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    //
    implementation(Deps.kotlin)
    implementation(Deps.datastore)
    implementation(Deps.jupiter)
    androidTestImplementation(Deps.archCoreTesting)
    androidTestImplementation(Deps.testRunner)
    //
    implementation(HiltDeps.hiltAndroid)
    implementation(HiltDeps.hiltNavigation)
    kapt(HiltDeps.hiltAndroidCompiler)
    implementation(HiltDeps.hiltTestAndroid)
    kaptTest(HiltDeps.hiltAndroidTestAnnotationProcessor)
    androidTestImplementation(HiltDeps.hiltTestAndroid)
    kaptAndroidTest(HiltDeps.hiltAndroidTestAnnotationProcessor)
    //
    implementation(Deps.mockk)
    implementation(Deps.coroutinesTest)
    //
    implementation(project(":commonUtils"))
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
