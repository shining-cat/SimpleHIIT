/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.session.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.session.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.sharedui.session.SessionViewState
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SessionErrorStateContent(
    screenViewState: SessionViewState.Error,
    navigateUp: () -> Boolean,
    onAbort: () -> Unit,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(dimensionResource(CommonResourcesR.dimen.spacing_1))
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier =
                    Modifier
                        .size(dimensionResource(R.dimen.error_symbol_size))
                        .padding(
                            horizontal = 0.dp,
                            vertical = dimensionResource(CommonResourcesR.dimen.spacing_2),
                        ),
                painter = painterResource(id = CommonResourcesR.drawable.warning),
                contentDescription = stringResource(id = CommonResourcesR.string.warning_icon_content_description),
            )
            Text(
                textAlign = TextAlign.Center,
                modifier =
                    Modifier.padding(
                        horizontal = dimensionResource(CommonResourcesR.dimen.spacing_3),
                        vertical = dimensionResource(CommonResourcesR.dimen.spacing_2),
                    ),
                text =
                    if (screenViewState.errorCode == DomainError.SESSION_NOT_FOUND.code) {
                        stringResource(id = CommonResourcesR.string.error_session_abort)
                    } else {
                        stringResource(id = CommonResourcesR.string.error_session_retry)
                    },
                style = MaterialTheme.typography.headlineMedium,
            )
            if (screenViewState.errorCode.isNotBlank()) {
                Text(
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .padding(
                                horizontal = dimensionResource(CommonResourcesR.dimen.spacing_3),
                                vertical = dimensionResource(CommonResourcesR.dimen.spacing_2),
                            ),
                    text =
                        stringResource(
                            id = CommonResourcesR.string.error_code,
                            screenViewState.errorCode,
                        ),
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
            val clickAction: () -> Unit = {
                if (screenViewState.errorCode == DomainError.SESSION_NOT_FOUND.code) {
                    onAbort()
                } else {
                    navigateUp()
                }
            }
            Button(
                modifier =
                    Modifier
                        .padding(
                            horizontal = 0.dp,
                            vertical = dimensionResource(CommonResourcesR.dimen.spacing_2),
                        ),
                onClick = clickAction,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
            ) {
                Text(
                    text =
                        if (screenViewState.errorCode == DomainError.SESSION_NOT_FOUND.code) {
                            stringResource(id = CommonResourcesR.string.abort_session_button_label)
                        } else {
                            stringResource(id = CommonResourcesR.string.exit_button_label)
                        },
                )
            }
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun SessionErrorStateContentPreview(
    @PreviewParameter(SessionErrorStateContentPreviewParameterProvider::class) sessionViewState: SessionViewState.Error,
) {
    SimpleHiitMobileTheme {
        Surface {
            SessionErrorStateContent(
                modifier = Modifier,
                screenViewState = sessionViewState,
                navigateUp = { true },
                onAbort = {},
            )
        }
    }
}

internal class SessionErrorStateContentPreviewParameterProvider : PreviewParameterProvider<SessionViewState.Error> {
    override val values: Sequence<SessionViewState.Error>
        get() =
            sequenceOf(
                SessionViewState.Error(errorCode = "ABCD-123"),
                SessionViewState.Error(errorCode = ""),
                SessionViewState.Error(errorCode = DomainError.SESSION_NOT_FOUND.code),
            )
}
