package fr.shiningcat.simplehiit.domain.common.models

/**
 * Domain-level error codes for business logic failures.
 * Used by domain use cases and consumed by UI for error handling and user feedback.
 */
enum class DomainError(
    val code: String,
) {
    DATABASE_FETCH_FAILED("0101"),
    DATABASE_INSERT_FAILED("0102"),
    DATABASE_DELETE_FAILED("0103"),
    DATABASE_UPDATE_FAILED("0104"),
    NO_USERS_FOUND("0201"),
    NO_USER_PROVIDED("0202"),
    USER_NAME_TAKEN("0203"),
    NO_SELECTED_USERS_FOUND("0204"),
    EMPTY_RESULT("0301"),
    CONVERSION_ERROR("0401"),
    SESSION_NOT_FOUND("0501"),
    LANGUAGE_SET_FAILED("0601"),
}
