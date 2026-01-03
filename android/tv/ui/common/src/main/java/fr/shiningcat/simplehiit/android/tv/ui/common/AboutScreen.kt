package fr.shiningcat.simplehiit.android.tv.ui.common

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.net.toUri
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.getAppVersion
import fr.shiningcat.simplehiit.android.shared.core.Screen
import fr.shiningcat.simplehiit.android.tv.ui.common.components.NavigationSideBar
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AboutScreen(navigateTo: (Screen) -> Unit) {
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationSideBar(
            navigateTo = navigateTo,
            currentDestination = Screen.About,
            showStatisticsButton = true,
        )
        AboutContent()
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun AboutContent() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Row(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(dimensionResource(R.dimen.spacing_4)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_4)),
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            val titleWithVersion = "${stringResource(R.string.app_name)} - ${getAppVersion()}"
            Text(
                modifier =
                    Modifier
                        .padding(top = dimensionResource(R.dimen.spacing_1))
                        .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = titleWithVersion,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                modifier =
                    Modifier
                        .padding(top = dimensionResource(R.dimen.spacing_2))
                        .fillMaxWidth(),
                text = stringResource(R.string.about_page_hiit_description),
                style = MaterialTheme.typography.bodyLarge,
            )
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth(.5f)
                        .aspectRatio(1f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = dimensionResource(R.dimen.spacing_3)),
                painter = painterResource(id = R.drawable.launcher_figure_and_clock_bicolor_themed),
                contentDescription = stringResource(id = R.string.about_page_figure_and_clock_icon_content_description),
            )
            Text(
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_3)),
                text = stringResource(R.string.about_page_app_features_part_1),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_1)),
                text = stringResource(R.string.about_page_app_features_part_2),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_1)),
                text = stringResource(R.string.about_page_app_features_part_3),
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                modifier =
                    Modifier
                        .padding(top = dimensionResource(R.dimen.spacing_1))
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.about_page_credit_label),
                    style = MaterialTheme.typography.bodyMedium,
                )
                val poseMyArtLink = stringResource(R.string.about_page_credit_link)
                Surface(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, poseMyArtLink.toUri())
                        context.startActivity(intent)
                    },
                    colors = ClickableSurfaceDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Text(
                        text = " ${stringResource(R.string.about_page_credit_name)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                    )
                }
            }
            val githubLink = stringResource(R.string.about_page_project_github_link)
            Surface(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, githubLink.toUri())
                    context.startActivity(intent)
                },
                colors = ClickableSurfaceDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
                modifier =
                    Modifier
                        .padding(top = dimensionResource(R.dimen.spacing_3))
                        .fillMaxWidth(),
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.about_page_project_github),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                )
            }
            Text(
                modifier =
                    Modifier
                        .padding(top = dimensionResource(R.dimen.spacing_3))
                        .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.about_page_app_by),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Image(
                modifier =
                    Modifier
                        .fillMaxWidth(.5f)
                        .aspectRatio(1f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = dimensionResource(R.dimen.spacing_1)),
                painter = painterResource(id = R.drawable.kiwi),
                contentDescription = stringResource(id = R.string.about_page_kiwi_logo_description),
            )
            Text(
                modifier =
                    Modifier
                        .padding(top = dimensionResource(R.dimen.spacing_1))
                        .fillMaxWidth(),
                text = stringResource(R.string.about_page_app_by_signature),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

// Previews
@PreviewTvScreensNoUi
@Composable
private fun AboutScreenPreview() {
    SimpleHiitTvTheme {
        Surface {
            AboutScreen(
                navigateTo = {},
            )
        }
    }
}
