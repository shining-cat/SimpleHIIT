package fr.shiningcat.simplehiit.android.common.ui.previews

import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Devices.FOLDABLE
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Phone", device = PHONE, showSystemUi = false)
@Preview(
    name = "Phone - Landscape",
    device = "spec:width=411dp,height=891dp,orientation=landscape,dpi=420",
    showSystemUi = false,
)
@Preview(name = "Unfolded Foldable", device = FOLDABLE, showSystemUi = false)
@Preview(name = "Tablet", device = TABLET, showSystemUi = false)
@Preview(name = "Desktop", device = DESKTOP, showSystemUi = false)
annotation class PreviewMobileScreensNoUI
