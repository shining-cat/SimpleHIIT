package fr.shiningcat.simplehiit.domain.common.models

enum class AppLanguage(
    val languageTag: String?,
) {
    SYSTEM_DEFAULT(null),
    ENGLISH("en"),
    FRENCH("fr"),
    SWEDISH("sv"),
}
