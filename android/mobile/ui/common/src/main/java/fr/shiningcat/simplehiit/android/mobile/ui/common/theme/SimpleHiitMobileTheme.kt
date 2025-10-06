package fr.shiningcat.simplehiit.android.mobile.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun SimpleHiitMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            darkTheme -> mobileDarkColorScheme
            else -> mobileLightColorScheme
        }

    val shapes =
        Shapes(
            small = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_2)),
            medium = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_2)),
            large = RoundedCornerShape(0.dp),
        )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = mobileType,
        shapes = shapes, // Use the locally defined shapes
        content = content,
    )
}
