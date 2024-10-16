plugins {
    kotlin("kapt")
    id("com.android.test")
    kotlin("android")
    alias(libs.plugins.simplehiit.hilt)
    jacoco
}

android {
    namespace = "fr.shiningcat.simplehiit.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
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

dependencies {
    implementation(projects.domain.common)
    implementation(projects.commonUtils)
    implementation(projects.testUtils)
    implementation(projects.data)
    //
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.coroutines)
    kapt(libs.androidx.room.compiler)
    //
    implementation(libs.hilt.android.testing)
    implementation(libs.jetbrains.coroutines.test)
    implementation(libs.androidx.room.testing)
    implementation(libs.androidx.archcore.testing)
    implementation(libs.test.runner)
    implementation(libs.junit)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
