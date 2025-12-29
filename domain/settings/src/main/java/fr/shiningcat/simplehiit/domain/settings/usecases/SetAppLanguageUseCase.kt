package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.LanguageRepository
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import javax.inject.Inject

class SetAppLanguageUseCase
    @Inject
    constructor(
        private val languageRepository: LanguageRepository,
        private val logger: HiitLogger,
    ) {
        suspend fun execute(language: AppLanguage): Output<Int> = languageRepository.setAppLanguage(language)
    }
