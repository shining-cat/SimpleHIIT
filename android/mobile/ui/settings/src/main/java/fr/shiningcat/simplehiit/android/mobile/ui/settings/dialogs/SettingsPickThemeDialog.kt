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
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptiveDialogProperties
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptiveDialogWidth
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.AdaptiveDialogButtonsLayout
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.ButtonType
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.DialogButtonConfig
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SettingsPickThemeDialog(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onCancel: () -> Unit,
) {
    var selectedTheme by remember { mutableStateOf(currentTheme) }

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
                            .padding(dimensionResource(CommonResourcesR.dimen.spacing_1))
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = CommonResourcesR.string.theme_dialog_pick_title),
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    AppTheme.entries.forEach { theme ->
                        ThemeOption(
                            theme = theme,
                            selected = selectedTheme == theme,
                            onSelected = { selectedTheme = it },
                        )
                    }
                    // Warning text about app restart
                    Text(
                        textAlign = TextAlign.Center,
                        text = stringResource(id = CommonResourcesR.string.theme_change_restart_warning),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(CommonResourcesR.dimen.spacing_2)),
                    )
                    SettingsPickThemeDialogButtonsLayout(
                        primaryButtonLabel = stringResource(CommonResourcesR.string.ok),
                        onPrimaryClick = { onThemeSelected(selectedTheme) },
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
                ).padding(vertical = dimensionResource(CommonResourcesR.dimen.spacing_05)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // null because the Row handles the click
        )
        Text(
            text = getThemeDisplayName(theme),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = dimensionResource(CommonResourcesR.dimen.spacing_1)),
        )
    }
}

@Composable
private fun SettingsPickThemeDialogButtonsLayout(
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

@Composable
private fun getThemeDisplayName(theme: AppTheme): String =
    when (theme) {
        AppTheme.LIGHT -> stringResource(id = CommonResourcesR.string.theme_light)
        AppTheme.DARK -> stringResource(id = CommonResourcesR.string.theme_dark)
        AppTheme.FOLLOW_SYSTEM -> stringResource(id = CommonResourcesR.string.theme_follow_system)
    }

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun SettingsPickThemeDialogPreviewLight() {
    SimpleHiitMobileTheme {
        Surface {
            SettingsPickThemeDialog(
                currentTheme = AppTheme.LIGHT,
                onThemeSelected = {},
                onCancel = {},
            )
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun SettingsPickThemeDialogPreviewDark() {
    SimpleHiitMobileTheme {
        Surface {
            SettingsPickThemeDialog(
                currentTheme = AppTheme.DARK,
                onThemeSelected = {},
                onCancel = {},
            )
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun SettingsPickThemeDialogPreviewFollowSystem() {
    SimpleHiitMobileTheme {
        Surface {
            SettingsPickThemeDialog(
                currentTheme = AppTheme.FOLLOW_SYSTEM,
                onThemeSelected = {},
                onCancel = {},
            )
        }
    }
}
