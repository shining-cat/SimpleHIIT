package fr.shiningcat.simplehiit.android.tv.ui.settings.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.tv.ui.common.components.RadioButtonsDialog
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SettingsPickThemeDialog(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onCancel: () -> Unit,
) {
    val themes = AppTheme.entries
    val selectedIndex = themes.indexOf(currentTheme)

    RadioButtonsDialog(
        title = stringResource(id = CommonResourcesR.string.theme_dialog_pick_title),
        options = themes.map { stringResource(id = getThemeDisplayNameResId(it)) },
        selectedIndex = selectedIndex,
        explanationText = stringResource(id = CommonResourcesR.string.theme_change_restart_warning),
        onOptionSelected = { index -> onThemeSelected(themes[index]) },
        onCancel = onCancel,
    )
}

/**
 * Returns the string resource ID for the display name of the given theme.
 *
 * @param theme The theme to get the display name for
 * @return The string resource ID for the theme's display name
 */
private fun getThemeDisplayNameResId(theme: AppTheme): Int =
    when (theme) {
        AppTheme.LIGHT -> CommonResourcesR.string.theme_light
        AppTheme.DARK -> CommonResourcesR.string.theme_dark
        AppTheme.FOLLOW_SYSTEM -> CommonResourcesR.string.theme_follow_system
    }

// Previews
@PreviewTvScreensNoUi
@Composable
private fun SettingsPickThemeDialogPreviewLight() {
    SimpleHiitTvTheme {
        Surface {
            SettingsPickThemeDialog(
                currentTheme = AppTheme.LIGHT,
                onThemeSelected = {},
                onCancel = {},
            )
        }
    }
}

@PreviewTvScreensNoUi
@Composable
private fun SettingsPickThemeDialogPreviewDark() {
    SimpleHiitTvTheme {
        Surface {
            SettingsPickThemeDialog(
                currentTheme = AppTheme.DARK,
                onThemeSelected = {},
                onCancel = {},
            )
        }
    }
}

@PreviewTvScreensNoUi
@Composable
private fun SettingsPickThemeDialogPreviewFollowSystem() {
    SimpleHiitTvTheme {
        Surface {
            SettingsPickThemeDialog(
                currentTheme = AppTheme.FOLLOW_SYSTEM,
                onThemeSelected = {},
                onCancel = {},
            )
        }
    }
}
