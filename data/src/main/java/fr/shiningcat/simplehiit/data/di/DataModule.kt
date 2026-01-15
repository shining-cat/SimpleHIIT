/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
@file:KeepForKoin

package fr.shiningcat.simplehiit.data.di

import fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin
import fr.shiningcat.simplehiit.data.mappers.SessionMapper
import fr.shiningcat.simplehiit.data.mappers.UserMapper
import fr.shiningcat.simplehiit.data.repositories.LanguageRepositoryImpl
import fr.shiningcat.simplehiit.data.repositories.SessionsRepositoryImpl
import fr.shiningcat.simplehiit.data.repositories.SettingsRepositoryImpl
import fr.shiningcat.simplehiit.data.repositories.UsersRepositoryImpl
import fr.shiningcat.simplehiit.domain.common.datainterfaces.LanguageRepository
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SessionsRepository
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule =
    module {
        // Mappers
        factory { UserMapper() }
        factory { SessionMapper() }

        // Repositories
        single<UsersRepository> {
            UsersRepositoryImpl(
                usersDao = get(),
                userMapper = get(),
                ioDispatcher = get(named("IoDispatcher")),
                hiitLogger = get(),
            )
        }

        single<SessionsRepository> {
            SessionsRepositoryImpl(
                sessionsDao = get(),
                sessionMapper = get(),
                ioDispatcher = get(named("IoDispatcher")),
                hiitLogger = get(),
            )
        }

        single<SettingsRepository> {
            SettingsRepositoryImpl(
                simpleHiitDataStoreManager = get(),
                ioDispatcher = get(named("IoDispatcher")),
                hiitLogger = get(),
            )
        }

        single<LanguageRepository> {
            LanguageRepositoryImpl(
                localeManager = get(),
                hiitLogger = get(),
            )
        }
    }
