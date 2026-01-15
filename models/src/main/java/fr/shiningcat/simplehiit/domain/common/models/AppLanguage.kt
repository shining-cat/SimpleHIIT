/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.common.models

enum class AppLanguage(
    val languageTag: String?,
) {
    SYSTEM_DEFAULT(null),
    ENGLISH("en"),
    FRENCH("fr"),
    SWEDISH("sv"),
}
