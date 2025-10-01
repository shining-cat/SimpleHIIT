package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.R
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun ChoiceDialog(
    title: String = "",
    @DrawableRes image: Int = -1,
    @StringRes imageContentDescription: Int = -1,
    message: String = "",
    primaryButtonLabel: String,
    primaryAction: () -> Unit,
    secondaryButtonLabel: String = "",
    secondaryAction: () -> Unit = {},
    dismissButtonLabel: String = stringResource(id = CommonResourcesR.string.cancel_button_label),
    dismissAction: () -> Unit,
) {
    Dialog(onDismissRequest = dismissAction) {
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
                if (title.isNotBlank()) {
                    Text(
                        textAlign = TextAlign.Left,
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (image != -1) {
                    Image(
                        modifier =
                            Modifier
                                .size(adaptDpToFontScale(dimensionResource(R.dimen.dialog_main_icon_size)))
                                .align(Alignment.CenterHorizontally)
                                .padding(
                                    horizontal = 0.dp,
                                    vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                                ),
                        painter = painterResource(id = image),
                        contentDescription = stringResource(id = imageContentDescription),
                    )
                }
                Text(
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier.padding(
                            horizontal = 0.dp,
                            vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                        ),
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(
                    Modifier
                        .padding(
                            horizontal = 0.dp,
                            vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                        ),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_3)),
                ) {
                    if (secondaryButtonLabel.isNotBlank()) {
                        ButtonText(
                            modifier =
                                Modifier
                                    .height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height)))
                                    .weight(1f),
                            fillWidth = true,
                            fillHeight = true,
                            onClick = secondaryAction,
                            label = secondaryButtonLabel,
                        )
                    }
                    if (dismissButtonLabel.isNotBlank()) {
                        ButtonBordered(
                            modifier =
                                Modifier
                                    .height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height)))
                                    .weight(1f),
                            fillWidth = true,
                            fillHeight = true,
                            onClick = dismissAction,
                            label = dismissButtonLabel,
                        )
                    }
                    ButtonFilled(
                        modifier =
                            Modifier
                                .height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height)))
                                .weight(1f),
                        fillWidth = true,
                        fillHeight = true,
                        onClick = primaryAction,
                        label = primaryButtonLabel,
                    )
                }
            }
        }
    }
}

// Previews
@PreviewTvScreensNoUi
@Composable
private fun ChoiceDialogPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            ChoiceDialog(
                message = "This will erase all users, all stored sessions, and all settings",
                primaryButtonLabel = "Yeah",
                primaryAction = {},
                secondaryButtonLabel = "Maybe",
                dismissButtonLabel = "Nope",
                dismissAction = {},
            )
        }
    }
}
