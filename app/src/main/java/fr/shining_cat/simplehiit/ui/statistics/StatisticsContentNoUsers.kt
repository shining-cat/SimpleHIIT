package fr.shining_cat.simplehiit.ui.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.R

@Composable
fun StatisticsContentNoUsers() {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.doge),
            contentDescription = stringResource(id = R.string.doge_icon_content_description),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 16.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.no_users_found_error_message),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}
