package fr.shiningcat.simplehiit.domain.common.models

/**
 * Validation error types for user input.
 * Used by domain validators and consumed by UI for feedback.
 */
enum class InputError {
    NONE,
    WRONG_FORMAT,
    TOO_LONG,
    VALUE_EMPTY,
    VALUE_TOO_SMALL,
    VALUE_TOO_BIG,
    VALUE_ALREADY_TAKEN,
}
