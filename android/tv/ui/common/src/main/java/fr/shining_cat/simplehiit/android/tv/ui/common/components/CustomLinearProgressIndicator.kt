package fr.shining_cat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.commonutils.HiitLogger

/**
 * The LinearProgressIndicator used in the mobile version is not (yet) available in the material TV library.
 * we need to draw one ourselves, as the recommendation is to always avoid mixing the mobile and tv compose libraries
 * This one only handles the "definite" version of the other one
 * one can provide various min and max values, and the current value is handled as an Int instead of a percentage
 * @param currentValue: the current value, between minValue and maxValue, progress will be calculated as a ratio of current vs maxValue & minValue
 * @param minValue
 * @param maxValue
 * @param progressColor: the color to be used to show the arc representing the currentValue
 * @param trackColor: optional color for the track (full line) won't be drawn if left null
 * @param borderColor: optional color for the border (full line) won't be drawn if left null
 */
@Composable
fun CustomLinearProgressIndicator(
    modifier: Modifier = Modifier,
    currentValue: Int,
    minValue: Int = 0,
    maxValue: Int = 100,
    progressColor: Color,
    trackColor: Color? = null,
    borderColor: Color? = null,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null
) {
    Box(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (borderColor != null) Modifier.border(
                        width = 1.dp,
                        color = borderColor
                    ) else Modifier
                )
        ) {
            //TRACK
            if (trackColor != null) {
                drawRect(
                    color = trackColor,
                    size = size
                )
            }

            //PROGRESS
            val unit = size.width / (maxValue - minValue)
            val progress = (currentValue - minValue) * unit
            drawRect(
                color = progressColor,
                size = Size(width = progress, height = size.height)
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewCustomLinearProgressIndicator() {
    SimpleHiitTvTheme {
        Surface(modifier = Modifier
            .width(300.dp)
            .height(150.dp),
            shape = MaterialTheme.shapes.extraSmall) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                CustomLinearProgressIndicator(
                    modifier = Modifier
                        .width(250.dp)
                        .height(24.dp)
                        .background(MaterialTheme.colorScheme.background),
                    currentValue = 7,
                    minValue = 0,
                    maxValue = 12,
                    trackColor = MaterialTheme.colorScheme.primary,
                    progressColor = MaterialTheme.colorScheme.secondary
                )
                CustomLinearProgressIndicator(
                    modifier = Modifier
                        .width(250.dp)
                        .height(24.dp)
                        .background(MaterialTheme.colorScheme.background),
                    currentValue = 33,
                    borderColor = MaterialTheme.colorScheme.primary,
                    progressColor = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}