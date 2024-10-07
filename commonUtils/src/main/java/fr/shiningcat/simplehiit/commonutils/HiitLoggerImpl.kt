package fr.shiningcat.simplehiit.commonutils

import android.util.Log

class HiitLoggerImpl(
    private val isDebugBuild: Boolean,
) : HiitLogger {
    private val TAG = "SIMPLEHIIT"

    override fun d(
        tag: String,
        msg: String,
    ) {
        if (isDebugBuild) {
            Log.d(TAG, "$tag::$msg")
        }
    }

    override fun e(
        tag: String,
        msg: String,
        throwable: Throwable?,
    ) {
        if (isDebugBuild) {
            if (throwable == null) {
                Log.e(TAG, "$tag::$msg")
            } else {
                Log.e(TAG, "$tag::$msg", throwable)
            }
        }
    }
}
