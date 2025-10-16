package fr.shiningcat.simplehiit.android.mobile.ui.statistics.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsTopAppBar(
    navigateUp: () -> Boolean = { true },
    allUsers: List<User>?,
    onUserSelected: (User) -> Unit,
) {
    var showUserMenu by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                    contentDescription = stringResource(id = R.string.back_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.statistics_page_title),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        actions = {
            if (allUsers != null && allUsers.size > 1) {
                Box {
                    IconButton(onClick = { showUserMenu = true }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.switch_user),
                            contentDescription = stringResource(id = R.string.statistics_page_switch_user),
                            tint = MaterialTheme.colorScheme.onPrimary,
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
        },
    )
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun StatisticsTopAppBarPreview(
    @PreviewParameter(StatisticsTopAppBarPreviewParameterProvider::class) previewData: List<User>,
) {
    SimpleHiitMobileTheme {
        Surface {
            StatisticsTopAppBar(
                allUsers = previewData,
                onUserSelected = {},
            )
        }
    }
}

private class StatisticsTopAppBarPreviewParameterProvider : PreviewParameterProvider<List<User>> {
    override val values: Sequence<List<User>>
        get() =
            sequenceOf(
                emptyList(),
                listOf(
                    User(name = "Alice"),
                ),
                listOf(
                    User(name = "Alice"),
                    User(name = "Bob"),
                    User(name = "Charlie"),
                ),
                listOf(
                    User(name = "Alice"),
                    User(name = "Bob"),
                    User(name = "Charlie"),
                    User(name = "David"),
                    User(name = "Emma"),
                    User(name = "Frank"),
                ),
            )
}
