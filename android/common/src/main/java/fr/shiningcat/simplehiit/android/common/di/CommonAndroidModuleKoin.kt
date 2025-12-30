package fr.shiningcat.simplehiit.android.common.di

import fr.shiningcat.simplehiit.android.common.NavigationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonAndroidModule =
    module {
        viewModel { NavigationViewModel(get()) }
    }
