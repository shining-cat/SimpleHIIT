/**
 * To define dependencies
 */
object Deps {
    val appCompat by lazy { "androidx.appcompat:appcompat:${Versions.APP_COMPAT}" }
    val datastore by lazy { "androidx.datastore:datastore-preferences:${Versions.DATASTORE}" }
    val materialDesign by lazy { "com.google.android.material:material:${Versions.MATERIAL}" }
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
    val hiltAndroidTestAnnotationProcessor by lazy { "com.google.dagger:hilt-android-compiler:${Versions.HILT}" }

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
