package fr.shining_cat.simplehiit.android.mobile.ui.home.contents

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.android.mobile.ui.home.components.NumberCyclesComponent
import fr.shining_cat.simplehiit.commonresources.R

@Composable
fun HomeMissingUsersContent(
    decreaseNumberOfCycles: () -> Unit = {},
    increaseNumberOfCycles: () -> Unit = {},
    numberOfCycles: Int,
    lengthOfCycle: String,
    totalLengthFormatted: String,
    navigateToSettings: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        NumberCyclesComponent(
            decreaseNumberOfCycles = decreaseNumberOfCycles,
            increaseNumberOfCycles = increaseNumberOfCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle,
            totalLengthFormatted = totalLengthFormatted
        )
        SelectUsersNoUsersComponent(navigateToSettings)
    }
}

@Composable
private fun SelectUsersNoUsersComponent(navigateToSettings: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { navigateToSettings() }
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.selected_users_setting_title),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.warning_no_user_exist),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 24.dp)
        )
        Image(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 0.dp, vertical = 24.dp),
            painter = painterResource(id = R.drawable.warning),
            contentDescription = stringResource(id = R.string.warning_icon_content_description)
        )
    }
}

// Previews
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HomeMissingUsersContentPreview() {
    SimpleHiitTheme {
        Surface {
            HomeMissingUsersContent(
                numberOfCycles = 5,
                lengthOfCycle = "4mn",
                totalLengthFormatted = "20mn"
            )
        }
    }
}
