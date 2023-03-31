package fr.shining_cat.simplehiit.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.R

@Composable
fun HomeMissingUsersContent(
    openInputNumberCycles: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    numberOfCycles: Int,
    lengthOfCycle: String
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        NumberCyclesComponent(
            openInputNumberCycles = openInputNumberCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle
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