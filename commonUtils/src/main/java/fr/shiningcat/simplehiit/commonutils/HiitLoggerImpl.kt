/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.commonutils

import android.util.Log

class HiitLoggerImpl(
    private val isDebugBuild: Boolean,
) : HiitLogger {
    private val tag = "SIMPLEHIIT"

    override fun d(
        tag: String,
        msg: String,
    ) {
        if (isDebugBuild) {
            Log.d(this.tag, "$tag::$msg")
        }
    }

    override fun e(
        tag: String,
        msg: String,
        throwable: Throwable?,
    ) {
        if (isDebugBuild) {
            if (throwable == null) {
                Log.e(this.tag, "$tag::$msg")
            } else {
                Log.e(this.tag, "$tag::$msg", throwable)
            }
        }
    }
}
