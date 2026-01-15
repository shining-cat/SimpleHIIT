/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
plugins {
    alias(libs.plugins.simplehiit.android.library)
    alias(libs.plugins.kover)
}

// Pure models module - no dependencies needed
dependencies {
    // No implementation dependencies - models are standalone
}
