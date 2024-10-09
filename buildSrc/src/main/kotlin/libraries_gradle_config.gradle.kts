plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    jacoco
}

repositories {
    google()
    mavenCentral()
}

android {
    compileSdk = ConfigData.LIBRARIES_COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = ConfigData.LIBRARIES_MIN_SDK_VERSION
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        getByName("debug") {
            enableUnitTestCoverage = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = ConfigData.JVM_TARGET
        // uncomment 2 lines below for a (really) verbose gradle output:
        // freeCompilerArgs = freeCompilerArgs + "-Xextended-compiler-checks"
        // verbose = true
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
