package fr.shiningcat.simplehiit.android.mobile.ui.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun StatisticsHeaderComponent(
    openUserPicker: () -> Unit = {},
    currentUserName: String,
    showUsersSwitch: Boolean,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    Row(
        modifier =
            Modifier
                .padding(dimensionResource(CommonResourcesR.dimen.spacing_2))
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            text = currentUserName,
        )
        if (showUsersSwitch) {
            Spacer(modifier = Modifier.width(dimensionResource(CommonResourcesR.dimen.spacing_4)))
            Button(
                modifier =
                    Modifier
                        .height(dimensionResource(CommonResourcesR.dimen.minimum_touch_size))
                        .width(adaptDpToFontScale(dimensionResource(R.dimen.switch_user_button_width))),
                onClick = openUserPicker,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(CommonResourcesR.drawable.switch_user),
                    contentDescription = stringResource(id = CommonResourcesR.string.statistics_page_switch_user),
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
            }
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun StatisticsHeaderPreview() {
    SimpleHiitMobileTheme {
        Surface {
            StatisticsHeaderComponent(
                currentUserName = "Charles-Antoine",
                showUsersSwitch = true,
            )
        }
    }
}
