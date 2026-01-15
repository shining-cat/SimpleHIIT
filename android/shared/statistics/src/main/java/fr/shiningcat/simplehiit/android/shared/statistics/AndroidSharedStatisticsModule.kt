/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
@file:KeepForKoin

package fr.shiningcat.simplehiit.android.shared.statistics

import fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val androidSharedStatisticsModule =
    module {

        viewModel {
            StatisticsViewModel(
                statisticsPresenter = get(),
                mainDispatcher = get(named("MainDispatcher")),
            )
        }
    }
