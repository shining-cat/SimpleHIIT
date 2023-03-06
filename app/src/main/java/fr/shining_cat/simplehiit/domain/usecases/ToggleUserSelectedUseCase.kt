package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class ToggleUserSelectedUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(user: User): Output<Int> {
        simpleHiitLogger.d("ToggleUserSelectedUseCase", "execute::user = $user")
        return simpleHiitRepository.updateUser(user)
    }
}