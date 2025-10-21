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
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.android.tv.ui.common.R as TvCommonR
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SettingsPickLanguageDialog(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onCancel: () -> Unit,
) {
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

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
                    text = stringResource(id = CommonResourcesR.string.language_dialog_pick_title),
                    style = MaterialTheme.typography.headlineSmall,
                )

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimensionResource(CommonResourcesR.dimen.spacing_2))
                            .selectableGroup(),
                ) {
                    AppLanguage.entries.forEach { language ->
                        LanguageOption(
                            language = language,
                            selected = selectedLanguage == language,
                            onSelected = { selectedLanguage = it },
                        )
                    }

                    // Explanation text
                    Text(
                        text = getRestartExplanation(selectedLanguage),
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
                        onClick = { onLanguageSelected(selectedLanguage) },
                        label = stringResource(id = CommonResourcesR.string.save_settings_button_label),
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageOption(
    language: AppLanguage,
    selected: Boolean,
    onSelected: (AppLanguage) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .selectable(
                    selected = selected,
                    onClick = { onSelected(language) },
                    role = Role.RadioButton,
                ).padding(vertical = dimensionResource(CommonResourcesR.dimen.spacing_1)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // null because the Row handles the click
        )
        Text(
            text = getLanguageDisplayName(language),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = dimensionResource(CommonResourcesR.dimen.spacing_1)),
        )
    }
}

private fun getLanguageDisplayName(language: AppLanguage): String =
    when (language) {
        AppLanguage.SYSTEM_DEFAULT -> "System default"
        AppLanguage.ENGLISH -> "English"
        AppLanguage.FRENCH -> "Français"
        AppLanguage.SWEDISH -> "Svenska"
    }

private fun getRestartExplanation(language: AppLanguage): String =
    when (language) {
        AppLanguage.SYSTEM_DEFAULT -> "The app will restart to apply the system language."
        AppLanguage.ENGLISH -> "The app will restart to apply the language change."
        AppLanguage.FRENCH -> "L'application redémarrera pour appliquer le changement de langue."
        AppLanguage.SWEDISH -> "Appen kommer att startas om för att tillämpa språkändringen."
    }

// Previews
@PreviewTvScreensNoUi
@Composable
private fun SettingsPickLanguageDialogPreviewSystemDefault() {
    SimpleHiitTvTheme {
        Surface {
            SettingsPickLanguageDialog(
                currentLanguage = AppLanguage.SYSTEM_DEFAULT,
                onLanguageSelected = {},
                onCancel = {},
            )
        }
    }
}

@PreviewTvScreensNoUi
@Composable
private fun SettingsPickLanguageDialogPreviewEnglish() {
    SimpleHiitTvTheme {
        Surface {
            SettingsPickLanguageDialog(
                currentLanguage = AppLanguage.ENGLISH,
                onLanguageSelected = {},
                onCancel = {},
            )
        }
    }
}

@PreviewTvScreensNoUi
@Composable
private fun SettingsPickLanguageDialogPreviewFrench() {
    SimpleHiitTvTheme {
        Surface {
            SettingsPickLanguageDialog(
                currentLanguage = AppLanguage.FRENCH,
                onLanguageSelected = {},
                onCancel = {},
            )
        }
    }
}

@PreviewTvScreensNoUi
@Composable
private fun SettingsPickLanguageDialogPreviewSwedish() {
    SimpleHiitTvTheme {
        Surface {
            SettingsPickLanguageDialog(
                currentLanguage = AppLanguage.SWEDISH,
                onLanguageSelected = {},
                onCancel = {},
            )
        }
    }
}
