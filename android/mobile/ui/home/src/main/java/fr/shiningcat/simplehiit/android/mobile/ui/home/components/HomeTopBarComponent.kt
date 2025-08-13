package fr.shiningcat.simplehiit.android.mobile.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.HomeViewState
import fr.shiningcat.simplehiit.commonresources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBarComponent(
    navigateTo: (String) -> Unit = {},
    screenViewState: HomeViewState,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        actions = {
            IconButton(onClick = { navigateTo(fr.shiningcat.simplehiit.android.common.Screen.Settings.route) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.cog),
                    contentDescription = stringResource(id = R.string.settings_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
            if (screenViewState is HomeViewState.Nominal) {
                IconButton(onClick = { navigateTo(fr.shiningcat.simplehiit.android.common.Screen.Statistics.route) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.bar_chart),
                        contentDescription = stringResource(id = R.string.statistics_button_content_label),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        },
    )
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun HomeTopBarComponentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Column(verticalArrangement = Arrangement.spacedBy(28.dp)) {
                HomeTopBarComponent(
                    screenViewState =
                        HomeViewState.Nominal(
                            numberCumulatedCycles = 2,
                            cycleLength = "2mn",
                            users = emptyList(),
                            totalSessionLengthFormatted = "20mn 34s",
                        ),
                )
                HomeTopBarComponent(
                    screenViewState =
                        HomeViewState.MissingUsers(
                            numberCumulatedCycles = 2,
                            cycleLength = "2mn",
                            totalSessionLengthFormatted = "20mn 34s",
                        ),
                )
            }
        }
    }
}
