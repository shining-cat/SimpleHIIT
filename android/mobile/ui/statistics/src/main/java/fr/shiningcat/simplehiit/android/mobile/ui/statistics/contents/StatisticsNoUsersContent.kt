/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.statistics.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.currentUiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.components.StatisticsHeaderComponent
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun StatisticsNoUsersContent(
    modifier: Modifier = Modifier,
    uiArrangement: UiArrangement,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(
                    top = if (uiArrangement == UiArrangement.VERTICAL) dimensionResource(R.dimen.spacing_1) else 0.dp,
                    start = dimensionResource(R.dimen.spacing_1),
                    end = dimensionResource(R.dimen.spacing_1),
                ).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StatisticsHeaderComponent(
            currentUserName = stringResource(R.string.no_users_found_page_title),
            showUsersSwitch = false, // No users available
            uiArrangement = uiArrangement,
            openUserPicker = {}, // Not used when no users
        )

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.crying_cat_multicolor),
            contentDescription = stringResource(id = R.string.sad_cat_icon_content_description),
            modifier = Modifier.padding(dimensionResource(R.dimen.spacing_1)),
        )
        Text(
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .padding(horizontal = 0.dp)
                    .fillMaxWidth(),
            text = stringResource(id = R.string.no_users_found_error_message),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun StatisticsNoUsersContentPreview() {
    val previewUiArrangement = currentUiArrangement()
    SimpleHiitMobileTheme {
        Surface {
            StatisticsNoUsersContent(
                modifier = Modifier.fillMaxSize(),
                uiArrangement = previewUiArrangement,
            )
        }
    }
}
