plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    jacoco
}

android {
    compileSdk = ConfigData.compileSdkVersion
    buildToolsVersion = ConfigData.buildToolsVersion

    defaultConfig {
        minSdk = ConfigData.minSdkVersion
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
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
