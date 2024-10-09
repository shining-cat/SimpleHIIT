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
    const val APPLICATION_ID = "fr.shiningcat.simplehiit"

    // Handheld devices config data:
    const val HANDHELD_COMPILE_SDK_VERSION = 34
    const val HANDHELD_MIN_SDK_VERSION = 21
    const val HANDHELD_TARGET_SDK_VERSION = 34
    const val HANDHELD_VERSION_CODE = 21100003
    const val HANDHELD_VERSION_NAME = "0.03"

    // TV config data:
    const val TV_COMPILE_SDK_VERSION = 34
    const val TV_MIN_SDK_VERSION = 21
    const val TV_TARGET_SDK_VERSION = 34
    const val TV_VERSION_CODE = 21010003
    const val TV_VERSION_NAME = "0.03"

    // libraries config data: these should be the lowest of tv and handheld respective values
    const val LIBRARIES_MIN_SDK_VERSION = 21
    const val LIBRARIES_COMPILE_SDK_VERSION = 34

    // JVM
    const val JVM_TARGET = "17"
}
