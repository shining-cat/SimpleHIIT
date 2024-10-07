/**
 * To define dependencies
 */
object Deps {
    val appCompat by lazy { "androidx.appcompat:appcompat:${Versions.APP_COMPAT}" }
    val datastore by lazy { "androidx.datastore:datastore-preferences:${Versions.DATASTORE}" }
    val materialDesign by lazy { "com.google.android.material:material:${Versions.MATERIAL}" }
    val androidXCoreKtx by lazy { "androidx.core:core-ktx:${Versions.ANDROIDX_CORE_KTX}" }
    val androidXLifeCycleProcess by lazy { "androidx.lifecycle:lifecycle-process:${Versions.ANDROIDX_LIFECYCLE_PROCESS}" }
    val coil by lazy { "io.coil-kt:coil-compose:${Versions.COIL}" }
    val coilGif by lazy { "io.coil-kt:coil-gif:${Versions.COIL}" }
}

object TestDeps {
    val testRunner by lazy { "androidx.test:runner:${Versions.TEST_RUNNER}" }
    val jupiter by lazy { "org.junit.jupiter:junit-jupiter:${Versions.JUPITER}" }
    val mockk by lazy { "io.mockk:mockk:${Versions.MOCKK_VERSION}" }
    val junit by lazy { "androidx.test.ext:junit:${Versions.JUNIT}" }
    val coroutinesTest by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES_TEST}" }
    val archCoreTesting by lazy { "androidx.arch.core:core-testing:${Versions.CORE_TESTING}" }
}

object HiltDeps {
    val hiltAndroid by lazy { "com.google.dagger:hilt-android:${Versions.HILT}" }
    val hiltAndroidCompiler by lazy { "com.google.dagger:hilt-compiler:${Versions.HILT}" }
    val hiltAndroidTestAnnotationProcessor by lazy { "com.google.dagger:hilt-android-compiler:${Versions.HILT}" }

    // hilt and navigation
    val hiltNavigation by lazy { "androidx.hilt:hilt-navigation-compose:${Versions.HILT_NAVIGATION}" }

    // For tests
    val hiltTestAndroid by lazy { "com.google.dagger:hilt-android-testing:${Versions.HILT}" }
}

object RoomDeps {
    val roomRuntime by lazy { "androidx.room:room-runtime:${Versions.ROOM_VERSION}" }

    // To use Kotlin annotation processing tool (kapt)
    val roomKaptCompiler by lazy { "androidx.room:room-compiler:${Versions.ROOM_VERSION}" }

    // optional - Kotlin Extensions and Coroutines support for Room
    val roomCoroutinesExtensions by lazy { "androidx.room:room-ktx:${Versions.ROOM_VERSION}" }

    // optional - Test helpers
    val roomTestHelpers by lazy { "androidx.room:room-testing:${Versions.ROOM_VERSION}" }
}

object ComposeDeps {
    // Material Design 3
    val composeMaterial3 by lazy { "androidx.compose.material3:material3" }
    val composeMaterial3WindowSize by lazy {
        "androidx.compose.material3:material3-window-size-class:${Versions.COMPOSE_MATERIAL3_WINDOW_SIZE_CLASS}"
    }
    val composeTVMaterial3 by lazy { "androidx.tv:tv-material:${Versions.MATERIAL_TV}" }
    val composeTVFoundation by lazy { "androidx.tv:tv-foundation:${Versions.COMPOSE_TV}" }

    // Android Studio Preview support
    val composePreview by lazy { "androidx.compose.ui:ui-tooling-preview" }
    val composePreviewDebug by lazy { "androidx.compose.ui:ui-tooling" }

    // UI Tests
    val composeUiTests by lazy { "androidx.compose.ui:ui-test-junit4" }
    val composeUiTestsDebug by lazy { "androidx.compose.ui:ui-test-manifest" }
}

object Navigation {
    // Kotlin
    val navFragments by lazy { "androidx.navigation:navigation-fragment-ktx:${Versions.COMPOSE_NAV_VERSION}" }
    val navUi by lazy { "androidx.navigation:navigation-ui-ktx:${Versions.COMPOSE_NAV_VERSION}" }

    // Jetpack Compose Integration
    val navCompose by lazy { "androidx.navigation:navigation-compose:${Versions.COMPOSE_NAV_VERSION}" }

    // Testing Navigation
    val navTesting by lazy { "androidx.navigation:navigation-testing:${Versions.COMPOSE_NAV_VERSION}" }
}
