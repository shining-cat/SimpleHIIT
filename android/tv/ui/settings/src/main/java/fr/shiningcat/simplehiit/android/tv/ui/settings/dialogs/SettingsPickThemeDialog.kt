package fr.shiningcat.simplehiit.android.tv.ui.settings.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.RadioButton
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonBordered
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonFilled
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.android.tv.ui.common.R as TvCommonR
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SettingsPickThemeDialog(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onCancel: () -> Unit,
) {
    var selectedTheme by remember { mutableStateOf(currentTheme) }

    Dialog(onDismissRequest = onCancel) {
        Surface(
            colors =
                SurfaceDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(dimensionResource(CommonResourcesR.dimen.spacing_1))
                        .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    textAlign = TextAlign.Left,
                    text = stringResource(id = CommonResourcesR.string.theme_dialog_pick_title),
                    style = MaterialTheme.typography.headlineSmall,
                )

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimensionResource(CommonResourcesR.dimen.spacing_2))
                            .selectableGroup(),
                ) {
                    AppTheme.entries.forEach { theme ->
                        ThemeOption(
                            theme = theme,
                            selected = selectedTheme == theme,
                            onSelected = { selectedTheme = it },
                        )
                    }

                    // Restart explanation text
                    Text(
                        text = getRestartExplanation(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(CommonResourcesR.dimen.spacing_2)),
                        textAlign = TextAlign.Center,
                    )
                }

                Row(
                    Modifier
                        .padding(
                            horizontal = 0.dp,
                            vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                        ),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_3)),
                ) {
                    ButtonBordered(
                        modifier =
                            Modifier
                                .height(adaptDpToFontScale(dimensionResource(TvCommonR.dimen.dialog_standard_button_height)))
                                .weight(1f),
                        fillWidth = true,
                        fillHeight = true,
                        onClick = onCancel,
                        label = stringResource(id = CommonResourcesR.string.cancel_button_label),
                    )
                    ButtonFilled(
                        modifier =
                            Modifier
                                .height(adaptDpToFontScale(dimensionResource(TvCommonR.dimen.dialog_standard_button_height)))
                                .weight(1f),
                        fillWidth = true,
                        fillHeight = true,
                        onClick = { onThemeSelected(selectedTheme) },
                        label = stringResource(id = CommonResourcesR.string.save_settings_button_label),
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeOption(
    theme: AppTheme,
    selected: Boolean,
    onSelected: (AppTheme) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .selectable(
                    selected = selected,
                    onClick = { onSelected(theme) },
                    role = Role.RadioButton,
                ).padding(vertical = dimensionResource(CommonResourcesR.dimen.spacing_1)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // null because the Row handles the click
        )
        Text(
            text = stringResource(id = getThemeDisplayNameResId(theme)),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = dimensionResource(CommonResourcesR.dimen.spacing_1)),
        )
    }
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

@Composable
private fun getRestartExplanation(): String = stringResource(id = CommonResourcesR.string.theme_change_restart_warning)

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
