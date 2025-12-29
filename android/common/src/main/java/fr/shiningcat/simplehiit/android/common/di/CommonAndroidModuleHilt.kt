package fr.shiningcat.simplehiit.android.common.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Temporary Hilt module during migration to Koin.
 * This module exists to maintain Hilt compatibility while the migration is in progress.
 *
 * DELETE THIS FILE after full migration to Koin is complete.
 *
 * Note: NavigationViewModel uses @HiltViewModel which automatically handles DI,
 * so no explicit @Provides needed here.
 */
@Module
@InstallIn(ViewModelComponent::class)
object CommonAndroidModuleHilt
