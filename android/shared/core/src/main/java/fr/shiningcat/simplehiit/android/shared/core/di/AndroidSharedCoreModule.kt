/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
@file:KeepForKoin

package fr.shiningcat.simplehiit.android.shared.core.di

import fr.shiningcat.simplehiit.android.shared.core.NavigationViewModel
import fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val androidSharedCoreModule =
    module {
        viewModel { NavigationViewModel(get()) }
    }
