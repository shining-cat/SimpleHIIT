plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    jacoco
}

android {
    namespace = "fr.shining_cat.simplehiit.data"

    compileSdk = ConfigData.compileSdkVersion
    buildToolsVersion = ConfigData.buildToolsVersion

    defaultConfig {
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion

        testInstrumentationRunner = "fr.shining_cat.simplehiit.testutils.HiltTestRunner"
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
            isMinifyEnabled = false
            enableUnitTestCoverage = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + "-Xextended-compiler-checks"
        verbose = true
    }

}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(project(":commonDomain"))
    implementation(project(":commonUtils"))
    testImplementation(project(":testUtils"))
    androidTestImplementation(project(":testUtils"))
    //
    implementation(HiltDeps.hiltAndroid)
    kapt(HiltDeps.hiltAndroidCompiler)
    implementation(Deps.datastore)
    implementation(RoomDeps.roomRuntime)
    implementation(RoomDeps.roomCoroutinesExtensions)
    kapt(RoomDeps.roomKaptCompiler)
    //
    testImplementation(HiltDeps.hiltTestAndroid)
    testImplementation(Deps.coroutinesTest)
    testImplementation(Deps.mockk)
    testImplementation(Deps.jupiter)
    //
    androidTestImplementation(HiltDeps.hiltTestAndroid)
    androidTestImplementation(Deps.coroutinesTest)
    androidTestImplementation(RoomDeps.roomTestHelpers)
    androidTestImplementation(Deps.archCoreTesting)
    androidTestImplementation(Deps.testRunner)
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
