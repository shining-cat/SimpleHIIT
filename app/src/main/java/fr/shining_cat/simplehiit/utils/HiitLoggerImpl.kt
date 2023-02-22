package fr.shining_cat.simplehiit.utils

import android.util.Log
import javax.inject.Inject

class HiitLoggerImpl(
    private val isDebugBuild: Boolean
) : HiitLogger {

    override fun d(tag: String, msg: String) {
        if (isDebugBuild) {
            Log.d(tag, msg)
        }
    }

    override fun e(tag: String, msg: String, throwable: Throwable?) {
        if (isDebugBuild) {
            if (throwable == null) {
                Log.e(tag, msg)
            } else {
                Log.e(tag, msg, throwable)
            }
        }
    }
}