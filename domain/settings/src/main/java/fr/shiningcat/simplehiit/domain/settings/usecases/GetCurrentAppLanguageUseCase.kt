package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.datainterfaces.LanguageRepository
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentAppLanguageUseCase
    @Inject
    constructor(
        private val languageRepository: LanguageRepository,
    ) {
        fun execute(): Flow<AppLanguage> = languageRepository.getCurrentLanguage()
    }
