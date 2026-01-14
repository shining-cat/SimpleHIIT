@file:KeepForKoin

package fr.shiningcat.simplehiit.android.shared.home

import fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val androidSharedHomeModule =
    module {

        viewModel {
            HomeViewModel(
                homePresenter = get(),
                mainDispatcher = get(named("MainDispatcher")),
            )
        }
    }
