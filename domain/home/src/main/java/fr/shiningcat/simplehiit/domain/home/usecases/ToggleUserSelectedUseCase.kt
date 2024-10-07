package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.di.DefaultDispatcher
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToggleUserSelectedUseCase
    @Inject
    constructor(
        private val simpleHiitRepository: SimpleHiitRepository,
        @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
        private val simpleHiitLogger: HiitLogger,
    ) {
        suspend fun execute(user: User): Output<Int> =
            withContext(defaultDispatcher) {
                simpleHiitLogger.d("ToggleUserSelectedUseCase", "execute::user = $user")
                simpleHiitRepository.updateUser(user)
            }
    }
