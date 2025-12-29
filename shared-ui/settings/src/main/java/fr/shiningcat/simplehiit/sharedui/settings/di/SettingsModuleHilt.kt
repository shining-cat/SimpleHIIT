@file:Suppress("ktlint:standard:filename") // Temporary during Hilt->Koin migration

package fr.shiningcat.simplehiit.sharedui.settings.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import fr.shiningcat.simplehiit.sharedui.settings.SettingsInteractor
import fr.shiningcat.simplehiit.sharedui.settings.SettingsInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
interface SettingsModule {
    @Binds
    fun bindSettingsInteractor(SettingsInteractor: SettingsInteractorImpl): SettingsInteractor
}
