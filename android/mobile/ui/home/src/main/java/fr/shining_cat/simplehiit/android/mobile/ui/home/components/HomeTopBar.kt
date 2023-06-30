package fr.shining_cat.simplehiit.android.mobile.ui.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import fr.shining_cat.simplehiit.android.mobile.ui.common.Screen
import fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState
import fr.shining_cat.simplehiit.commonresources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    navigateTo: (String) -> Unit = {},
    screenViewState: HomeViewState
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(onClick = { navigateTo(Screen.Settings.route) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.cog),
                    contentDescription = stringResource(id = R.string.settings_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            if (screenViewState is HomeViewState.Nominal) {
                IconButton(onClick = { navigateTo(Screen.Statistics.route) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.bar_chart),
                        contentDescription = stringResource(id = R.string.statistics_button_content_label),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}