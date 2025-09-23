package fr.shiningcat.simplehiit.android.mobile.ui.common.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.commonresources.R

val mobileShapes =
    Shapes(
        small = RoundedCornerShape(R.dimen.corner_radius_2),
        medium = RoundedCornerShape(R.dimen.corner_radius_2),
        large = RoundedCornerShape(0.dp),
    )
