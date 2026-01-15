/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.statistics.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.math.round

/**
 * This calculates an average over an arbitrary 7-days period,
 * rather than any form of predefined calendar week
 */
class CalculateAverageSessionsPerWeekUseCase(
    private val defaultDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    private val weekLengthAsMs = 604800000L

    suspend fun execute(
        timestamps: List<Long>,
        now: Long,
    ): String =
        withContext(defaultDispatcher) {
            if (timestamps.isEmpty()) {
                "0"
            } else {
                val elapsedTotalTimeMs = now - timestamps.min()
                val numberOfElapsedWeeks = elapsedTotalTimeMs.toDouble() / weekLengthAsMs.toDouble()
                val numberOfSessions = timestamps.size
                if (numberOfElapsedWeeks < 1) {
                    logger.d(
                        "CalculateAverageSessionsPerWeekUseCase",
                        "less than a week between now and oldest session - returning total number of sessions",
                    )
                    numberOfSessions.toString()
                } else {
                    val averageSessionsPer7DaysPeriod =
                        numberOfSessions.toDouble() / numberOfElapsedWeeks
                    val roundedAverageSessionsPer7DaysPeriod =
                        round(averageSessionsPer7DaysPeriod * 100.0) / 100.0
                    if (roundedAverageSessionsPer7DaysPeriod - roundedAverageSessionsPer7DaysPeriod.toInt() == 0.00) {
                        // remove trailing 0
                        roundedAverageSessionsPer7DaysPeriod.toInt().toString()
                    } else {
                        roundedAverageSessionsPer7DaysPeriod.toString()
                    }
                }
            }
        }
}
