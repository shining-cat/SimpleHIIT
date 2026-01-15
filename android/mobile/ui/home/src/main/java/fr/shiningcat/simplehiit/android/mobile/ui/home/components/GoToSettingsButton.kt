/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.home.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.R
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun GoToSettingsButton(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit = {},
) {
    Button(
        modifier =
            modifier
                .padding(vertical = dimensionResource(CommonResourcesR.dimen.spacing_3))
                .height(dimensionResource(R.dimen.large_button_height)),
        onClick = navigateToSettings,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
    ) {
        Text(text = stringResource(id = CommonResourcesR.string.go_to_settings))
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun GoToSettingsButtonPreview() {
    SimpleHiitMobileTheme {
        Surface {
            GoToSettingsButton()
        }
    }
}
