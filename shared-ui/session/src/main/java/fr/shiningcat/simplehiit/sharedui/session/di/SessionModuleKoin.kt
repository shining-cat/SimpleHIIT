@file:KeepForKoin

package fr.shiningcat.simplehiit.sharedui.session.di

import fr.shiningcat.simplehiit.commonutils.annotations.KeepForKoin
import fr.shiningcat.simplehiit.sharedui.session.SessionInteractor
import fr.shiningcat.simplehiit.sharedui.session.SessionInteractorImpl
import fr.shiningcat.simplehiit.sharedui.session.SessionPresenter
import fr.shiningcat.simplehiit.sharedui.session.SessionViewStateMapper
import fr.shiningcat.simplehiit.sharedui.session.SoundPoolFactory
import fr.shiningcat.simplehiit.sharedui.session.SoundPoolFactoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sessionModule =
    module {
        // ViewStateMapper - factory since it was ViewModelComponent scoped
        factory {
            SessionViewStateMapper(
                formatLongDurationMsAsSmallestHhMmSsStringUseCase = get(),
                defaultDispatcher = get(named("DefaultDispatcher")),
                logger = get(),
            )
        }

        // Interactor - factory since it was ViewModelComponent scoped
        factory<SessionInteractor> {
            SessionInteractorImpl(
                getSessionSettingsUseCase = get(),
                buildSessionUseCase = get(),
                formatLongDurationMsAsSmallestHhMmSsStringUseCase = get(),
                stepTimerUseCase = get(),
                insertSessionUseCase = get(),
            )
        }

        // SoundPoolFactory - singleton (stateless factory)
        single<SoundPoolFactory> { SoundPoolFactoryImpl() }

        // Presenter - factory since it holds mutable state
        factory {
            SessionPresenter(
                sessionInteractor = get(),
                mapper = get(),
                timeProvider = get(),
                dispatcher = get(named("MainDispatcher")),
                logger = get(),
            )
        }
    }
