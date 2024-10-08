package fr.shiningcat.simplehiit.domain.common.models

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class DisplayedStatistic(
    val displayValue: String,
    val type: DisplayStatisticType,
)

@ExcludeFromJacocoGeneratedReport
enum class DisplayStatisticType {
    TOTAL_SESSIONS_NUMBER,
    TOTAL_EXERCISE_TIME,
    AVERAGE_SESSION_LENGTH,
    LONGEST_STREAK,
    CURRENT_STREAK,
    AVERAGE_SESSIONS_PER_WEEK,
}
