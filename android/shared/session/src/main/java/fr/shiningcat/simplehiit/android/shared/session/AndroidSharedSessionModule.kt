package fr.shiningcat.simplehiit.android.shared.session

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val androidSharedSessionModule =
    module {
        viewModel {
            SessionViewModel(
                presenter = get(),
                soundPoolFactory = get(),
                mainDispatcher = get(named("MainDispatcher")),
                logger = get(),
            )
        }
    }
