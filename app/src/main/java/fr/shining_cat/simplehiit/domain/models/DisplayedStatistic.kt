package fr.shining_cat.simplehiit.domain.models

data class DisplayedStatistic(
    val displayValue: String,
    val type: DisplayStatisticType
)

enum class DisplayStatisticType{
    TOTAL_SESSIONS_NUMBER, TOTAL_EXERCISE_TIME, AVERAGE_SESSION_LENGTH, LONGEST_STREAK, CURRENT_STREAK, AVERAGE_SESSIONS_PER_WEEK
}