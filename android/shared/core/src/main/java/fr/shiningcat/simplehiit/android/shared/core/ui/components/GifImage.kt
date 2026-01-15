/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.shared.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import fr.shiningcat.simplehiit.commonresources.R

/**
 * This will load and display a DrawableRes as a Gif, and play it in loop
 * @param gifResId: the gif resource pointer
 * @param mirrored: whether to reverse the display along the vertical middle axis
 * @param paused: whether to freeze the GIF animation on the first frame
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    @DrawableRes
    gifResId: Int,
    contentDescription: String,
    mirrored: Boolean = false,
    paused: Boolean = false,
) {
    val imageModifier =
        if (mirrored) {
            modifier.graphicsLayer { rotationY = 180f }
        } else {
            modifier
        }

    if (paused) {
        GifFirstFrameImage(
            gifResId = gifResId,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    } else {
        // When not paused, display the animated GIF
        GlideImage(
            model = gifResId,
            contentDescription = contentDescription,
            modifier = imageModifier.fillMaxWidth(),
            failure = placeholder(R.drawable.cross_x),
        )
    }
}
