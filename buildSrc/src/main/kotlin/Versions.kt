object Versions {
    const val appCompat = "1.6.1"
    const val datastore = "1.0.0"
    const val material = "1.9.0"
    const val jupiter = "5.9.3"
    const val junit = "1.1.5"
    const val androidXcoreKtx = "1.10.1"
    const val hilt = "2.48.1"
    const val hiltNavigation = "1.0.0"
    const val room_version = "2.6.0" // latest stable version is 2.5.2, but is incompatible with kotlin 1.9+, breaking build on suspend methods. V.2.5.3 is expected to fix this but not out yet. see https://issuetracker.google.com/issues/236612358
    const val composeMaterial3WindowSizeClass = "1.1.2"
    const val nav_version = "2.7.3"
    const val mockkVersion = "1.13.8"
    const val coroutinesTest = "1.7.3"
    const val core_testing = "2.2.0"
    const val test_runner = "1.5.2"
    const val coil = "2.4.0"// upgrading to 2.5.0 version fails the build with "Could not resolve all dependencies" error listed in to-do file
    const val benManesDependenciesVersionPlugin = "0.49.0"
    const val gmazzoJacocoReportAggregationPlugin = "2.2.0"
    const val savvasdalkitsisDependencyGraphPlugin = "0.12"
    const val composeBom = "2023.06.01" // upgrading to any higher version fails the build with "Could not resolve all dependencies" error listed in to-do file
    const val activityCompose = "1.7.0"
    const val composeTV = "1.0.0-alpha09" // see https://developer.android.com/jetpack/androidx/releases/tv for releases announcements
    const val kotlinCompilerExtension = "1.5.3" // this has to be kept in sync with kotlin-gradle-plugin version. See https://developer.android.com/jetpack/androidx/releases/compose-kotlin for kotlin and compose compile version compatibility
    const val ktlintGradlePlugin = "11.6.1"
}