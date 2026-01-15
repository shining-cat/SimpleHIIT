/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.common.models

/**
 * Validation error types for user input.
 * Used by domain validators and consumed by UI for feedback.
 * Returns null when validation passes (no error).
 */
enum class InputError {
    WRONG_FORMAT,
    TOO_LONG,
    VALUE_EMPTY,
    VALUE_TOO_SMALL,
    VALUE_TOO_BIG,
    VALUE_ALREADY_TAKEN,
}
