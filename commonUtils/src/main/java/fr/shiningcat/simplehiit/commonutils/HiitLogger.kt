/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.commonutils

interface HiitLogger {
    fun d(
        tag: String,
        msg: String,
    )

    fun e(
        tag: String,
        msg: String,
        throwable: Throwable? = null,
    )
}
