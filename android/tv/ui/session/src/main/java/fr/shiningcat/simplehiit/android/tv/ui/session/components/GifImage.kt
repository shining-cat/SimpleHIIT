package fr.shiningcat.simplehiit.android.tv.ui.session.components

import android.os.Build.VERSION.SDK_INT
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size

/**
 * This will load and display a DrawableRes as a Gif, and play it in loop
 * @param gifResId: the gif resource pointer
 * @param mirrored: whether to reverse the display along the left-right middle axis
 */
@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    @DrawableRes
    gifResId: Int,
    mirrored: Boolean = false
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    val imageModifier = if (mirrored) {
        modifier.graphicsLayer { rotationY = 180f }
    } else {
        modifier
    }
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(data = gifResId)
                .apply(block = { size(Size.ORIGINAL) })
                .build(),
            imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = imageModifier.fillMaxWidth()
    )
}
