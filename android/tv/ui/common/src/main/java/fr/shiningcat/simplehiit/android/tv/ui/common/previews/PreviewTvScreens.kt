package fr.shiningcat.simplehiit.android.tv.ui.common.previews // Or any appropriate package

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

/**
 * A custom multi-preview annotation for typical TV screen characteristics.
 * This helps in visualizing composables on different TV resolutions and font scales.
 */
@Preview(
    name = "light mode, fontscale 1",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1f,
    uiMode = Configuration.UI_MODE_TYPE_TELEVISION or Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark mode, fontscale 1",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1f,
    uiMode = Configuration.UI_MODE_TYPE_TELEVISION or Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "light mode, fontscale 1.5",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1.5f,
    uiMode = Configuration.UI_MODE_TYPE_TELEVISION or Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark mode, fontscale 1.5",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1.5f,
    uiMode = Configuration.UI_MODE_TYPE_TELEVISION or Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "light mode, fontscale 2",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 2f,
    uiMode = Configuration.UI_MODE_TYPE_TELEVISION or Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark mode, fontscale 2",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 2f,
    uiMode = Configuration.UI_MODE_TYPE_TELEVISION or Configuration.UI_MODE_NIGHT_YES,
)
annotation class PreviewTvScreen
