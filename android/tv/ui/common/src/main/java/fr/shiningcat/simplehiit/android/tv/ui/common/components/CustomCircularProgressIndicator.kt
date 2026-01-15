/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.tv.ui.common.R
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

/**
 * The CircularProgressIndicator used in the mobile version is not (yet) available in the material TV library.
 * we need to draw one ourselves, as the recommendation is to always avoid mixing the mobile and tv compose libraries
 * This one only handles the "definite" version of the other one
 * one can provide various min and max values, and the current value is handled as an Int instead of a percentage
 * @param currentValue: the current value, between minValue and maxValue, progress will be calculated as a ratio of current vs maxValue & minValue
 * @param minValue
 * @param maxValue
 * @param circleRadius: the radius of the main circular component, in Px
 * @param thickness: the thickness of the track circle and the progress arc, in Px
 * @param progressColor: the color to be used to show the arc representing the currentValue
 * @param trackColor: optional color for the track (full circle) won't be drawn if left null
 * @param fillColor: optional color for filling the inside of the circle won't be drawn if left null
 * @param subDivisionsColor: optional color for the subdivisions won't be drawn if left null
 */
@Composable
fun CustomCircularProgressIndicator(
    modifier: Modifier = Modifier,
    currentValue: Int,
    minValue: Int = 0,
    maxValue: Int = 100,
    circleRadius: Float,
    thickness: Float,
    progressColor: Color,
    trackColor: Color? = null,
    fillColor: Color? = null,
    subDivisionsColor: Color? = null,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    val subdivisionsStrokeWidth = dimensionResource(R.dimen.border_thin_stroke)

    Box(
        modifier = modifier,
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val width = size.width
            val height = size.height
            circleCenter = Offset(x = width / 2f, y = height / 2f)

            // FILL
            if (fillColor != null) {
                drawCircle(
                    color = fillColor,
                    radius = circleRadius,
                    center = circleCenter,
                )
            }

            // TRACK
            if (trackColor != null) {
                drawCircle(
                    style =
                        Stroke(
                            width = thickness,
                        ),
                    color = trackColor,
                    radius = circleRadius,
                    center = circleCenter,
                )
            }

            // PROGRESS
            drawArc(
                color = progressColor,
                startAngle = 270f,
                sweepAngle = (360f / maxValue) * currentValue.toFloat(),
                style =
                    Stroke(
                        width = thickness,
                        cap = StrokeCap.Round,
                    ),
                useCenter = false,
                size =
                    Size(
                        width = circleRadius * 2f,
                        height = circleRadius * 2f,
                    ),
                topLeft =
                    Offset(
                        (width - circleRadius * 2f) / 2f,
                        (height - circleRadius * 2f) / 2f,
                    ),
            )

            // SUBDIVISIONS
            if (subDivisionsColor != null) {
                val outerRadius = circleRadius + thickness / 2f
                val gap = 15f
                for (i in 0..(maxValue - minValue)) {
                    val color =
                        if (i < currentValue - minValue) {
                            subDivisionsColor
                        } else {
                            subDivisionsColor.copy(
                                alpha = 0.3f,
                            )
                        }
                    val angleInDegrees = i * 360f / (maxValue - minValue).toFloat() + 180f
                    val angleInRad = angleInDegrees * PI / 180f + PI / 2f

                    val yGapAdjustment = cos(angleInDegrees * PI / 180f) * gap
                    val xGapAdjustment = -sin(angleInDegrees * PI / 180f) * gap

                    val start =
                        Offset(
                            x = (outerRadius * cos(angleInRad) + circleCenter.x + xGapAdjustment).toFloat(),
                            y = (outerRadius * sin(angleInRad) + circleCenter.y + yGapAdjustment).toFloat(),
                        )

                    val end =
                        Offset(
                            x = (outerRadius * cos(angleInRad) + circleCenter.x + xGapAdjustment).toFloat(),
                            y = (outerRadius * sin(angleInRad) + thickness + circleCenter.y + yGapAdjustment).toFloat(),
                        )

                    rotate(
                        angleInDegrees,
                        pivot = start,
                    ) {
                        drawLine(
                            color = color,
                            start = start,
                            end = end,
                            strokeWidth = subdivisionsStrokeWidth.toPx(),
                        )
                    }
                }
            }
        }
    }
}

@PreviewFontScale
@PreviewLightDark
@Composable
fun PreviewCustomCircularProgressIndicator() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_2))) {
                CustomCircularProgressIndicator(
                    modifier =
                        Modifier
                            .size(250.dp)
                            .background(MaterialTheme.colorScheme.background),
                    currentValue = 7,
                    minValue = 0,
                    maxValue = 12,
                    trackColor = MaterialTheme.colorScheme.primary,
                    progressColor = MaterialTheme.colorScheme.secondary,
                    circleRadius = 250f,
                    thickness = 25f,
                )
                CustomCircularProgressIndicator(
                    modifier =
                        Modifier
                            .size(250.dp)
                            .background(MaterialTheme.colorScheme.background),
                    currentValue = 33,
                    trackColor = MaterialTheme.colorScheme.primary,
                    progressColor = MaterialTheme.colorScheme.secondary,
                    circleRadius = 250f,
                    thickness = 25f,
                    fillColor = MaterialTheme.colorScheme.primary.copy(alpha = .3f),
                    subDivisionsColor = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}
