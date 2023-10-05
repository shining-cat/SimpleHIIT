package fr.shiningcat.simplehiit.android.common.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import fr.shiningcat.simplehiit.commonutils.HiitLogger

class StickyFooterArrangement(
    private val verticalPadding: Dp,
    private val hiitLogger: HiitLogger?
) : Arrangement.Vertical {

    //for some reason, if the resulting height of the grid or column is bigger than the visible space available (ie, if it needs to be scrollable)
    // then spacing will be used to render the padding between items and the rows' y value we set in Density.arrange will be ignored
    //however in the opposite case, spacing's value is ignored, and instead the rows' y value is used
    //thus we need to both override spacing to return our own value, and to include it in the rows' y calculation

    override val spacing: Dp
        get() = verticalPadding

    override fun Density.arrange(totalSize: Int, sizes: IntArray, outPositions: IntArray) {
        var y = 0
        sizes.forEachIndexed { index, size ->
            outPositions[index] = y
            y += (size + verticalPadding.toPx().toInt())
        }
        if (y < totalSize) {
            val lastIndex = outPositions.lastIndex
            outPositions[lastIndex] = totalSize - sizes.last()
        }
    }
}