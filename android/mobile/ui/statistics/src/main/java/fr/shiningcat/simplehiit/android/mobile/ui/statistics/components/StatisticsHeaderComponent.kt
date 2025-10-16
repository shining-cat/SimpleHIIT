package fr.shiningcat.simplehiit.android.mobile.ui.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun StatisticsHeaderComponent(
    currentUserName: String,
    allUsers: List<User>?,
    uiArrangement: UiArrangement,
    onUserSelected: (User) -> Unit = {},
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    var showUserMenu by remember { mutableStateOf(false) }

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
        if (uiArrangement == UiArrangement.HORIZONTAL && allUsers != null && allUsers.size > 1) {
            Spacer(modifier = Modifier.width(dimensionResource(CommonResourcesR.dimen.spacing_4)))
            Box {
                Button(
                    modifier =
                        Modifier
                            .height(dimensionResource(CommonResourcesR.dimen.minimum_touch_size))
                            .width(adaptDpToFontScale(dimensionResource(R.dimen.switch_user_button_width))),
                    onClick = { showUserMenu = true },
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
                DropdownMenu(
                    expanded = showUserMenu,
                    onDismissRequest = { showUserMenu = false },
                    containerColor = MaterialTheme.colorScheme.surface,
                ) {
                    allUsers.forEach { user ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = user.name,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            },
                            onClick = {
                                showUserMenu = false
                                onUserSelected(user)
                            },
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
@PreviewMobileScreensNoUI
@Composable
private fun StatisticsHeaderPreview() {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val previewUiArrangement: UiArrangement =
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) { // typically, a tablet or bigger in landscape
            UiArrangement.HORIZONTAL
        } else { // WindowWidthSizeClass.Medium, WindowWidthSizeClass.Compact :
            if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) { // typically, a phone in landscape
                UiArrangement.HORIZONTAL
            } else {
                UiArrangement.VERTICAL // typically, a phone or tablet in portrait
            }
        }
    SimpleHiitMobileTheme {
        Surface {
            StatisticsHeaderComponent(
                currentUserName = "Charles-Antoine",
                uiArrangement = previewUiArrangement,
                allUsers =
                    listOf(
                        User(name = "Alice"),
                        User(name = "Bob"),
                        User(name = "Charlie"),
                    ),
            )
        }
    }
}
