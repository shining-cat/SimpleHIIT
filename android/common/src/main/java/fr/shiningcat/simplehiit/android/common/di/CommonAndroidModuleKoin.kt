package fr.shiningcat.simplehiit.android.common.di

import fr.shiningcat.simplehiit.android.common.NavigationViewModel
import fr.shiningcat.simplehiit.android.common.viewmodels.HomeViewModel
import fr.shiningcat.simplehiit.android.common.viewmodels.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonAndroidModule =
    module {
        viewModel { NavigationViewModel(get()) }

        viewModel {
            HomeViewModel(
                homePresenter = get(),
                mainDispatcher = get(named("MainDispatcher")),
            )
        }

        viewModel {
            SettingsViewModel(
                presenter = get(),
                mainDispatcher = get(named("MainDispatcher")),
            )
        }
    }
