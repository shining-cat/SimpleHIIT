package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.datainterfaces.SettingsRepository
import fr.shiningcat.simplehiit.domain.common.models.AppTheme

/**
 * Sets the application theme preference
 *
 * @param theme The theme to set (LIGHT, DARK, or FOLLOW_SYSTEM)
 */
class SetAppThemeUseCase(
    private val settingsRepository: SettingsRepository,
) {
    suspend fun execute(theme: AppTheme) {
        settingsRepository.setAppTheme(theme)
    }
}
