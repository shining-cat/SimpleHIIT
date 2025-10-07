package fr.shiningcat.simplehiit.android.mobile.ui.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.R
import fr.shiningcat.simplehiit.domain.common.models.LaunchSessionWarning
import kotlinx.coroutines.launch
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchSessionButton(
    modifier: Modifier = Modifier,
    canLaunchSession: Boolean,
    navigateToSession: () -> Unit = {},
    launchSessionWarning: LaunchSessionWarning? = null,
) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Button(
            enabled = canLaunchSession,
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(vertical = dimensionResource(CommonResourcesR.dimen.spacing_3))
                    .height(dimensionResource(R.dimen.large_button_height)),
            onClick = navigateToSession,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                ),
        ) {
            if (canLaunchSession) {
                Text(text = stringResource(id = CommonResourcesR.string.launch_session_label))
            } else {
                Text(text = stringResource(id = CommonResourcesR.string.cannot_launch_session_label))
            }
        }

        launchSessionWarning?.let {
            val warningText =
                when (launchSessionWarning) {
                    LaunchSessionWarning.DUPLICATED_EXERCISES ->
                        stringResource(
                            id = CommonResourcesR.string.launch_session_warning_duplicated_exercises,
                        )

                    LaunchSessionWarning.SKIPPED_EXERCISE_TYPES ->
                        stringResource(
                            id = CommonResourcesR.string.launch_session_warning_skipped_exercise_types,
                        )

                    LaunchSessionWarning.NO_USER_SELECTED ->
                        stringResource(
                            id = CommonResourcesR.string.launch_session_warning_no_user_selected,
                        )
                }
            Box(
                modifier =
                    Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = dimensionResource(CommonResourcesR.dimen.spacing_2)),
            ) {
                TooltipBox(
                    positionProvider =
                        TooltipDefaults.rememberTooltipPositionProvider(
                            positioning = TooltipAnchorPosition.Above,
                        ),
                    tooltip = {
                        PlainTooltip(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            shadowElevation = 8.dp,
                        ) {
                            Text(text = warningText)
                        }
                    },
                    state = tooltipState,
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                tooltipState.show()
                            }
                        },
                        modifier =
                            Modifier
                                .size(dimensionResource(CommonResourcesR.dimen.minimum_touch_size)),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription =
                                stringResource(
                                    id = CommonResourcesR.string.launch_session_info_indicator_content_description,
                                ),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun LaunchSessionButtonPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Column {
                LaunchSessionButton(canLaunchSession = true)
                LaunchSessionButton(canLaunchSession = false)
                LaunchSessionButton(
                    canLaunchSession = true,
                    launchSessionWarning = LaunchSessionWarning.DUPLICATED_EXERCISES,
                )
                LaunchSessionButton(
                    canLaunchSession = false,
                    launchSessionWarning = LaunchSessionWarning.SKIPPED_EXERCISE_TYPES,
                )
            }
        }
    }
}
