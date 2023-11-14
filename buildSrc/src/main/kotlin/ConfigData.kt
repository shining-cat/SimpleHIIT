object ConfigData {
    // When deploying the same app for 2 different platforms, each app needs to have their own versionCode,
    // but a common applicationId (sometimes called application's package name)
    // Here we're keeping definitions for both so they can be compared to each other

    // VersionCode scheme: XXYYZZZZ where
    //      XX represents the min API level supported
    //      YY represents the targeted device family:
    //          10 for handheld devices (uses-feature touchscreen required)
    //          01 for TV devices (uses-feature leanback required, touchscreen not required)
    //      ZZZZ is the compacted versionName so that app version 2.34 will be 0234

    // common applicationId
    const val applicationID = "fr.shiningcat.simplehiit"
    // Handheld devices config data:
    const val handheldCompileSdkVersion = 34
    const val handheldMinSdkVersion = 21
    const val handheldTargetSdkVersion = 34
    const val handheldVersionCode = 21100001
    const val handheldVersionName = "0.01"
    // TV config data:
    const val tvCompileSdkVersion = 34
    const val tvMinSdkVersion = 21
    const val tvTargetSdkVersion = 33
    const val tvVersionCode = 21010001
    const val tvVersionName = "0.01"
    //libraries config data: these should be the lowest of tv and handheld respective values
    const val librariesMinSdkVersion = 21
    const val librariesCompileSdkVersion = 33
}