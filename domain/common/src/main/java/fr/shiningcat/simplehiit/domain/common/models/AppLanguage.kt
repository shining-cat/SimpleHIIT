package fr.shiningcat.simplehiit.domain.common.models

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
enum class AppLanguage(
    val languageTag: String?,
) {
    SYSTEM_DEFAULT(null),
    ENGLISH("en"),
    FRENCH("fr"),
    SWEDISH("sv"),
}
