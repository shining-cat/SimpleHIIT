/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.common.models

/**
 * Represents a modification to the number of cycles.
 * Used across all layers to communicate cycle count change intent.
 */
enum class CyclesModification {
    INCREASE,
    DECREASE,
}
