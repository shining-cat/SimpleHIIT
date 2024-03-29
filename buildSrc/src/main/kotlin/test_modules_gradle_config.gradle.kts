plugins {
    id("com.android.test")
    kotlin("android")
    kotlin("kapt")
    jacoco
}

android {
    compileSdk = ConfigData.librariesCompileSdkVersion

    defaultConfig {
        minSdk = ConfigData.librariesMinSdkVersion
        testInstrumentationRunner = "fr.shiningcat.simplehiit.testutils.HiltTestRunner"
    }

    targetProjectPath = ":android:mobile:app"

    buildTypes {
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
        //uncomment 2 lines below when wanting a (really) verbose gradle output:
        //freeCompilerArgs = freeCompilerArgs + "-Xextended-compiler-checks"
        //verbose = true
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
