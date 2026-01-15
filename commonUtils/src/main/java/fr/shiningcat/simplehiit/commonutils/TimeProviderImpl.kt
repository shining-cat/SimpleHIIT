/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.commonutils

// We can't mock System classes, so this wrapper won't be tested
// there is no logic though, and it's really only a wrapper that allows testing other usecases of System.currentTimeMillis
// see: https://github.com/mockk/mockk/issues/98
class TimeProviderImpl : TimeProvider {
    override fun getCurrentTimeMillis(): Long = System.currentTimeMillis()
}
