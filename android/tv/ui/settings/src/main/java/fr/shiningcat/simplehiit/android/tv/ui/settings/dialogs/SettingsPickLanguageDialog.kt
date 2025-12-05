package fr.shiningcat.simplehiit.android.tv.ui.settings.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.tv.ui.common.components.RadioButtonsDialog
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SettingsPickLanguageDialog(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onCancel: () -> Unit,
) {
    val languages = AppLanguage.entries
    val selectedIndex = languages.indexOf(currentLanguage)
    var currentFocusedIndex by remember { mutableIntStateOf(selectedIndex) }

    val focusedLanguage = languages[currentFocusedIndex]
    val translationNotice = getTranslationNotice(focusedLanguage)
    val restartExplanation = getRestartExplanation(focusedLanguage)
    val fullExplanationText = "$translationNotice\n\n$restartExplanation"

    RadioButtonsDialog(
        title = stringResource(id = CommonResourcesR.string.language_dialog_pick_title),
        options = languages.map { getLanguageDisplayName(it) },
        selectedIndex = selectedIndex,
        explanationText = fullExplanationText,
        onOptionSelected = { index -> onLanguageSelected(languages[index]) },
        onFocusChanged = { index -> currentFocusedIndex = index },
        onCancel = onCancel,
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
        AppLanguage.SYSTEM_DEFAULT -> "(Translations have been automatically generated)"
        AppLanguage.ENGLISH -> "(Translations have been automatically generated)"
        AppLanguage.FRENCH -> "(Les traductions ont été générées automatiquement)"
        AppLanguage.SWEDISH -> "(Översättningar har genererats automatiskt)"
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
