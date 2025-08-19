package fr.shiningcat.simplehiit.android.mobile.ui.previews

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview annotation that represents various mobile devices, without showing the system UI.
 *
 * NOT showing device UI for use with dialogs
 *
 * Add this annotation to a composable to render multiple previews at once:
 * - Phone
 * - Unfolded Foldable
 * - Tablet
 * - Desktop
 *
 */
@Preview(name = "Phone", device = Devices.PHONE, showSystemUi = false)
@Preview(
    name = "Phone - Landscape",
    device = "spec:width=411dp,height=891dp,orientation=landscape,dpi=420",
    showSystemUi = false,
)
@Preview(name = "Unfolded Foldable", device = Devices.FOLDABLE, showSystemUi = false)
@Preview(name = "Tablet", device = Devices.TABLET, showSystemUi = false)
@Preview(name = "Desktop", device = Devices.DESKTOP, showSystemUi = false)
annotation class PreviewMobileScreensNoUI
