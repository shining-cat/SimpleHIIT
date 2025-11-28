package fr.shiningcat.simplehiit.android.tv.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.R
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonFilled
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonText
import fr.shiningcat.simplehiit.android.tv.ui.common.components.DialogContentLayout
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onAbort: () -> Unit,
) {
    val title = stringResource(id = CommonResourcesR.string.pause)
    val message = stringResource(id = CommonResourcesR.string.pause_explanation)
    val abortButtonLabel = stringResource(id = CommonResourcesR.string.abort_session_button_label)
    val resumeButtonLabel = stringResource(CommonResourcesR.string.resume_button_label)

    val resumeButtonFocusRequester = remember { FocusRequester() }
    var focusRequested by remember { mutableStateOf(false) }

    DialogContentLayout(
        onDismissRequest = onResume,
        title = title,
        content = {
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
        },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_3)),
            ) {
                ButtonText(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height)))
                            .weight(1f),
                    fillWidth = true,
                    fillHeight = true,
                    onClick = onAbort,
                    label = abortButtonLabel,
                )
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height)))
                            .weight(1f)
                            .focusRequester(resumeButtonFocusRequester)
                            .onGloballyPositioned {
                                if (!focusRequested) {
                                    resumeButtonFocusRequester.requestFocus()
                                    focusRequested = true
                                }
                            },
                    fillWidth = true,
                    fillHeight = true,
                    onClick = onResume,
                    label = resumeButtonLabel,
                )
            }
        },
    )
}

// Previews
@PreviewTvScreensNoUi
@Composable
private fun PauseDialogPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            PauseDialog(
                onAbort = {},
                onResume = {},
            )
        }
    }
}
