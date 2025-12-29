package fr.shiningcat.simplehiit.android.tv.app.di

import fr.shiningcat.simplehiit.android.common.di.commonAndroidModule
import fr.shiningcat.simplehiit.android.tv.app.MainViewModel
import fr.shiningcat.simplehiit.android.tv.app.locale.LocaleManagerImpl
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.AndroidVersionProvider
import fr.shiningcat.simplehiit.commonutils.AndroidVersionProviderImpl
import fr.shiningcat.simplehiit.commonutils.di.dispatchersModule
import fr.shiningcat.simplehiit.commonutils.di.utilsModule
import fr.shiningcat.simplehiit.data.di.dataModule
import fr.shiningcat.simplehiit.data.di.dispatcherModule
import fr.shiningcat.simplehiit.data.local.di.localDataModule
import fr.shiningcat.simplehiit.data.local.localemanager.LocaleManager
import fr.shiningcat.simplehiit.domain.common.DurationStringFormatter
import fr.shiningcat.simplehiit.domain.common.di.commonDomainModule
import fr.shiningcat.simplehiit.domain.home.di.homeDomainModule
import fr.shiningcat.simplehiit.domain.session.di.sessionDomainModule
import fr.shiningcat.simplehiit.domain.settings.di.settingsDomainModule
import fr.shiningcat.simplehiit.domain.statistics.di.statisticsDomainModule
import fr.shiningcat.simplehiit.sharedui.home.di.homeModule
import fr.shiningcat.simplehiit.sharedui.session.di.sessionModule
import fr.shiningcat.simplehiit.sharedui.settings.di.settingsModule
import fr.shiningcat.simplehiit.sharedui.statistics.di.statisticsModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * TV app-specific module.
 * Provides LocaleManager, AndroidVersionProvider, DurationStringFormatters, and MainViewModel.
 */
val tvAppModule =
    module {
        // LocaleManager
        single<LocaleManager> { LocaleManagerImpl(get(), get(), get()) }

        // AndroidVersionProvider
        single<AndroidVersionProvider> { AndroidVersionProviderImpl() }

        // DurationStringFormatter - Digits format
        single(named("DigitsFormat")) {
            DurationStringFormatter(
                hoursMinutesSeconds = androidContext().getString(R.string.hours_minutes_seconds_digits),
                hoursMinutesNoSeconds = androidContext().getString(R.string.hours_minutes_no_seconds_digits),
                hoursNoMinutesNoSeconds = androidContext().getString(R.string.hours_no_minutes_no_seconds_digits),
                minutesSeconds = androidContext().getString(R.string.minutes_seconds_digits),
                minutesNoSeconds = androidContext().getString(R.string.minutes_no_seconds_digits),
                seconds = androidContext().getString(R.string.seconds_digits),
            )
        }

        // DurationStringFormatter - Short format
        single(named("ShortFormat")) {
            DurationStringFormatter(
                hoursMinutesSeconds = androidContext().getString(R.string.hours_minutes_seconds_short),
                hoursMinutesNoSeconds = androidContext().getString(R.string.hours_minutes_no_seconds_short),
                hoursNoMinutesNoSeconds = androidContext().getString(R.string.hours_no_minutes_no_seconds_short),
                minutesSeconds = androidContext().getString(R.string.minutes_seconds_short),
                minutesNoSeconds = androidContext().getString(R.string.minutes_no_seconds_short),
                seconds = androidContext().getString(R.string.seconds_short),
            )
        }

        // MainViewModel
        viewModel { MainViewModel(get()) }
    }

/**
 * Aggregates all Koin modules for the TV app.
 * This provides a single list of modules to load in the Application class.
 */
val allKoinModules: List<Module> =
    listOf(
        // Foundation modules
        utilsModule,
        dispatchersModule,
        // Data layer
        dispatcherModule,
        localDataModule,
        dataModule,
        // Domain layer
        commonDomainModule,
        homeDomainModule,
        sessionDomainModule,
        settingsDomainModule,
        statisticsDomainModule,
        // Shared-UI layer
        homeModule,
        sessionModule,
        settingsModule,
        statisticsModule,
        // Android common
        commonAndroidModule,
        // App module
        tvAppModule,
    )
