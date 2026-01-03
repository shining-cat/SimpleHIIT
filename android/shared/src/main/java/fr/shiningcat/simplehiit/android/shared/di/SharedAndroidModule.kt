package fr.shiningcat.simplehiit.android.shared.di

import fr.shiningcat.simplehiit.android.shared.core.NavigationViewModel
import fr.shiningcat.simplehiit.android.shared.home.HomeViewModel
import fr.shiningcat.simplehiit.android.shared.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sharedAndroidModule =
    module {
        // === Core ===
        viewModel { NavigationViewModel(get()) }

        // === Home Feature ===
        viewModel {
            HomeViewModel(
                homePresenter = get(),
                mainDispatcher = get(named("MainDispatcher")),
            )
        }

        // === Settings Feature ===
        viewModel {
            SettingsViewModel(
                presenter = get(),
                mainDispatcher = get(named("MainDispatcher")),
            )
        }
    }
