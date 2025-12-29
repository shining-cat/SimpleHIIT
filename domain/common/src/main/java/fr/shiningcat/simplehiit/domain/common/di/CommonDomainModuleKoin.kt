package fr.shiningcat.simplehiit.domain.common.di

import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shiningcat.simplehiit.domain.common.usecases.ResetWholeAppUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonDomainModule =
    module {
        // Dispatcher
        single(named("TimerDispatcher")) { Dispatchers.Default }

        // UseCases
        single {
            ResetWholeAppUseCase(
                settingsRepository = get(),
                usersRepository = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        single {
            FormatLongDurationMsAsSmallestHhMmSsStringUseCase(
                durationStringFormatterDigits = get(named("DigitsFormat")),
                durationStringFormatterShort = get(named("ShortFormat")),
                logger = get(),
            )
        }
    }
