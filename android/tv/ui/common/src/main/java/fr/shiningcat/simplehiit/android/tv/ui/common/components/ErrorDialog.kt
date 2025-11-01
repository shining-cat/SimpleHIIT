package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
fun ErrorDialog(
    errorMessage: String,
    errorCode: String,
    dismissButtonLabel: String = "",
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
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 0.dp,
                                vertical = dimensionResource(CommonResourcesR.dimen.spacing_05),
                            ),
                    text = stringResource(id = CommonResourcesR.string.error_title),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Image(
                    modifier =
                        Modifier
                            .size(adaptDpToFontScale(dimensionResource(R.dimen.dialog_main_icon_size)))
                            .align(Alignment.CenterHorizontally)
                            .padding(
                                horizontal = 0.dp,
                                vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                            ),
                    painter = painterResource(id = CommonResourcesR.drawable.warning),
                    contentDescription = stringResource(id = CommonResourcesR.string.warning_icon_content_description),
                )
                Text(
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 0.dp,
                                vertical = dimensionResource(CommonResourcesR.dimen.spacing_1),
                            ),
                    text = stringResource(id = CommonResourcesR.string.error_notice),
                    style = MaterialTheme.typography.bodyMedium,
                )
                if (errorMessage.isNotBlank()) {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 0.dp,
                                    vertical = dimensionResource(CommonResourcesR.dimen.spacing_1),
                                ),
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Text(
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 0.dp,
                                vertical = dimensionResource(CommonResourcesR.dimen.spacing_1),
                            ),
                    text = stringResource(id = CommonResourcesR.string.error_code, errorCode),
                    style = MaterialTheme.typography.bodyMedium,
                )
                ButtonText(
                    modifier =
                        Modifier
                            .padding(top = dimensionResource(CommonResourcesR.dimen.spacing_3))
                            .fillMaxWidth(3f)
                            .height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height))),
                    fillWidth = true,
                    fillHeight = true,
                    label = dismissButtonLabel,
                    onClick = dismissAction,
                )
            }
        }
    }
}

// Previews
@PreviewTvScreensNoUi
@Composable
private fun ErrorDialogPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            ErrorDialog(
                errorMessage = "A balloon is floating above the country",
                errorCode = "1234",
                dismissAction = {},
                dismissButtonLabel = "OK",
            )
        }
    }
}
