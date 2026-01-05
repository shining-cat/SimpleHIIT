package fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.AdaptiveDialogButtonsLayout
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.ButtonType
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.DialogButtonConfig
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptiveDialogProperties
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptiveDialogWidth
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SettingsPickLanguageDialog(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onCancel: () -> Unit,
) {
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

    val dialogPadding = dimensionResource(CommonResourcesR.dimen.spacing_1)

    Dialog(
        onDismissRequest = onCancel,
        properties = adaptiveDialogProperties(),
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.adaptiveDialogWidth(),
        ) {
            BoxWithConstraints {
                val dialogAvailableWidthDp = this.maxWidth
                val effectiveDialogContentWidthDp = dialogAvailableWidthDp - 2 * dialogPadding
                Column(
                    modifier =
                        Modifier
                            .padding(dialogPadding)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = CommonResourcesR.string.language_dialog_pick_title),
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    AppLanguage.entries.forEach { language ->
                        LanguageOption(
                            language = language,
                            selected = selectedLanguage == language,
                            onSelected = { selectedLanguage = it },
                        )
                    }
                    Text(
                        textAlign = TextAlign.Center,
                        text = getTranslationNotice(selectedLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(CommonResourcesR.dimen.spacing_2)),
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = getRestartExplanation(selectedLanguage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(CommonResourcesR.dimen.spacing_1)),
                    )
                    SettingsPickLanguageDialogButtonsLayout(
                        primaryButtonLabel = stringResource(CommonResourcesR.string.ok),
                        onPrimaryClick = { onLanguageSelected(selectedLanguage) },
                        dismissButtonLabel = stringResource(CommonResourcesR.string.cancel_button_label),
                        onDismissClick = onCancel,
                        effectiveDialogContentWidthDp = effectiveDialogContentWidthDp,
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
                ).padding(vertical = dimensionResource(CommonResourcesR.dimen.spacing_05)),
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

@Composable
private fun SettingsPickLanguageDialogButtonsLayout(
    primaryButtonLabel: String,
    onPrimaryClick: () -> Unit,
    dismissButtonLabel: String?,
    onDismissClick: () -> Unit,
    effectiveDialogContentWidthDp: Dp,
) {
    val primaryButtonInfo =
        DialogButtonConfig(
            label = primaryButtonLabel,
            style = MaterialTheme.typography.labelMedium,
            type = ButtonType.FILLED,
            onClick = onPrimaryClick,
        )
    val dismissButtonInfo =
        if (dismissButtonLabel.isNullOrBlank().not()) {
            DialogButtonConfig(
                label = dismissButtonLabel,
                style = MaterialTheme.typography.labelMedium,
                type = ButtonType.TEXT,
                onClick = onDismissClick,
            )
        } else {
            null
        }

    val buttons = listOfNotNull(dismissButtonInfo, primaryButtonInfo)
    val buttonsSpacingDp = dimensionResource(CommonResourcesR.dimen.spacing_15)
    AdaptiveDialogButtonsLayout(
        buttons = buttons,
        modifier =
            Modifier.padding(
                horizontal = 0.dp,
                vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
            ),
        dialogContentWidthDp = effectiveDialogContentWidthDp,
        horizontalSpacingDp = buttonsSpacingDp,
        verticalSpacingDp = buttonsSpacingDp,
    )
}

private fun getLanguageDisplayName(language: AppLanguage): String =
    when (language) {
        AppLanguage.SYSTEM_DEFAULT -> "System default"
        AppLanguage.ENGLISH -> "English"
        AppLanguage.FRENCH -> "Français"
        AppLanguage.SWEDISH -> "Svenska"
    }

private fun getTranslationNotice(language: AppLanguage): String =
    when (language) {
        AppLanguage.SYSTEM_DEFAULT -> "(translations have been automatically generated)"
        AppLanguage.ENGLISH -> "(translations have been automatically generated)"
        AppLanguage.FRENCH -> "(les traductions ont été générées automatiquement)"
        AppLanguage.SWEDISH -> "(översättningar har genererats automatiskt)"
    }

private fun getRestartExplanation(language: AppLanguage): String =
    when (language) {
        AppLanguage.SYSTEM_DEFAULT -> "The app will restart to apply the system language."
        AppLanguage.ENGLISH -> "The app will restart to apply the language change."
        AppLanguage.FRENCH -> "L'application redémarrera pour appliquer le changement de langue."
        AppLanguage.SWEDISH -> "Appen kommer att startas om för att tillämpa språkändringen."
    }

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun SettingsPickLanguageDialogPreviewSystemDefault() {
    SimpleHiitMobileTheme {
        Surface {
            SettingsPickLanguageDialog(
                currentLanguage = AppLanguage.SYSTEM_DEFAULT,
                onLanguageSelected = {},
                onCancel = {},
            )
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun SettingsPickLanguageDialogPreviewEnglish() {
    SimpleHiitMobileTheme {
        Surface {
            SettingsPickLanguageDialog(
                currentLanguage = AppLanguage.ENGLISH,
                onLanguageSelected = {},
                onCancel = {},
            )
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun SettingsPickLanguageDialogPreviewFrench() {
    SimpleHiitMobileTheme {
        Surface {
            SettingsPickLanguageDialog(
                currentLanguage = AppLanguage.FRENCH,
                onLanguageSelected = {},
                onCancel = {},
            )
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun SettingsPickLanguageDialogPreviewSwedish() {
    SimpleHiitMobileTheme {
        Surface {
            SettingsPickLanguageDialog(
                currentLanguage = AppLanguage.SWEDISH,
                onLanguageSelected = {},
                onCancel = {},
            )
        }
    }
}
