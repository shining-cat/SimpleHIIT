/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
@file:KeepForKoin

package fr.shiningcat.simplehiit.domain.common.di

import fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shiningcat.simplehiit.domain.common.usecases.ResetWholeAppUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonDomainModule =
    module {
        // UseCases
        factory {
            ResetWholeAppUseCase(
                settingsRepository = get(),
                usersRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        factory {
            FormatLongDurationMsAsSmallestHhMmSsStringUseCase(
                durationStringFormatterDigits = get(named("DigitsFormat")),
                durationStringFormatterShort = get(named("ShortFormat")),
                logger = get(),
            )
        }
    }
