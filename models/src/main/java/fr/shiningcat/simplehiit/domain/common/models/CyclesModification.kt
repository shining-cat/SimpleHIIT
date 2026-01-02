package fr.shiningcat.simplehiit.domain.common.models

/**
 * Represents a modification to the number of cycles.
 * Used across all layers to communicate cycle count change intent.
 */
enum class CyclesModification {
    INCREASE,
    DECREASE,
}
