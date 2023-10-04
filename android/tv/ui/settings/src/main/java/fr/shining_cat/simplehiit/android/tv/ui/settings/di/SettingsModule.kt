package fr.shining_cat.simplehiit.android.tv.ui.settings.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.shining_cat.simplehiit.android.tv.ui.settings.SettingsInteractor
import fr.shining_cat.simplehiit.android.tv.ui.settings.SettingsInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface SettingsModule {

    @Binds
    fun bindSettingsInteractor(
        SettingsInteractor: SettingsInteractorImpl
    ): SettingsInteractor

}