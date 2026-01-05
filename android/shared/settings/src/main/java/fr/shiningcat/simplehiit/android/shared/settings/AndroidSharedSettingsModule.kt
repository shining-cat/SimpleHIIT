package fr.shiningcat.simplehiit.android.shared.settings

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val androidSharedSettingsModule =
    module {
        viewModel {
            SettingsViewModel(
                presenter = get(),
                mainDispatcher = get(named("MainDispatcher")),
            )
        }
    }
