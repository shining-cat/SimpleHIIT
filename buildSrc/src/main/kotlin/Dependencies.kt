import Versions.nav_version
import Versions.room_version

/**
 * To define plugins
 */
object BuildPlugins {
    val android by lazy { "com.android.tools.build:gradle:${Versions.gradlePlugin}" }
    val kotlin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}" }
}

/**
 * To define dependencies
 */
object Deps {
    val appCompat           by lazy { "androidx.appcompat:appcompat:${Versions.appCompat}" }
    val datastore           by lazy { "androidx.datastore:datastore-preferences:${Versions.datastore}" }
    val kotlin              by lazy { "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}" }
    val materialDesign      by lazy { "com.google.android.material:material:${Versions.material}" }
    val constraintLayout    by lazy { "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}" }
    val jupiter             by lazy { "org.junit.jupiter:junit-jupiter:${Versions.jupiter}" }
    val mockk               by lazy { "io.mockk:mockk:${Versions.mockkVersion}" }
    val coroutinesTest      by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}" }
    val archCoreTesting     by lazy { "androidx.arch.core:core-testing:${Versions.core_testing}" }
    val testRunner          by lazy { "androidx.test:runner:${Versions.test_runner}" }
}

object HiltDeps{
    val hiltAndroid         by lazy { "com.google.dagger:hilt-android:${Versions.hilt}"}
    val hiltAndroidCompiler by lazy { "com.google.dagger:hilt-compiler:${Versions.hilt}"}
    // For tests
    val hiltTestAndroid     by lazy { "com.google.dagger:hilt-android-testing:${Versions.hilt}"}
    val hiltAndroidTestAnnotationProcessor by lazy { "com.google.dagger:hilt-compiler:${Versions.hilt}"}

    //hilt and navigation
    val hiltNavigation      by lazy {"androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigation}"}
}

object RoomDeps{
    val roomRuntime         by lazy { "androidx.room:room-runtime:$room_version"}
    // To use Kotlin annotation processing tool (kapt)
    val roomKaptCompiler    by lazy { "androidx.room:room-compiler:$room_version"}
    // optional - Kotlin Extensions and Coroutines support for Room
    val roomCoroutinesExtensions by lazy { "androidx.room:room-ktx:$room_version"}
    // optional - Test helpers
    val roomTestHelpers     by lazy { "androidx.room:room-testing:$room_version"}
}

object ComposeDeps{
    // Material Design 3
    val composeMaterial3    by lazy { "androidx.compose.material3:material3"}
    // Android Studio Preview support
    val composePreview      by lazy { "androidx.compose.ui:ui-tooling-preview"}
    val composePreviewDebug by lazy { "androidx.compose.ui:ui-tooling"}
    // UI Tests
    val composeUiTests      by lazy { "androidx.compose.ui:ui-test-junit4"}
    val composeUiTestsDebug by lazy { "androidx.compose.ui:ui-test-manifest"}
    // Integration with activities
    val composeActivities   by lazy {"androidx.activity:activity-compose:1.5.1"}
    // Integration with ViewModels
    val composeViewModels   by lazy {"androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"}
    // Integration with LiveData
    val composeLiveData     by lazy {"androidx.compose.runtime:runtime-livedata"}
}

object Navigation{
    // Kotlin
    val navFragments    by lazy {"androidx.navigation:navigation-fragment-ktx:$nav_version"}
    val navUi           by lazy {"androidx.navigation:navigation-ui-ktx:$nav_version"}
    // Testing Navigation
    val navTesting      by lazy {"androidx.navigation:navigation-testing:$nav_version"}
    // Jetpack Compose Integration
    val navCompose      by lazy {"androidx.navigation:navigation-compose:$nav_version"}

}