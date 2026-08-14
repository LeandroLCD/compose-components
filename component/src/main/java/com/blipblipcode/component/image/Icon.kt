package com.blipblipcode.component.image

import androidx.compose.material3.Icon as MaterialIcon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * A thin wrapper around Material 3's [MaterialIcon] that adds [tintCap] and [tintStroke]
 * support for vector drawables.
 *
 * - [tintCap] controls which layers of the [imageVector] receive the fill [tint] color; the
 *   rest are rendered with their original fill colors.
 * - [tintStroke] is the optional color used to recolor the stroke of the layers selected by
 *   [tintStrokeCap]. When `null` (default) the vector's original strokes are preserved.
 *
 * @see TintCap
 * @see TintStroke
 */
@Composable
fun IconComponents(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    tintCap: TintCap = TintCap.All,
    tintStroke: Color? = null,
    tintStrokeCap: TintStroke = TintStroke.All,
) {
    val recolored: ImageVector? = remember(imageVector, tint, tintCap, tintStroke, tintStrokeCap) {
        val fillNeedsRebuild = !tintCap.isUndefined && tintCap !== TintCap.All
        // Material Icon's `tint` only recolors the fill. To recolor the stroke we always
        // need to rebuild the vector, even when tintStrokeCap is All.
        val strokeNeedsRebuild = tintStroke != null && !tintStrokeCap.isUndefined

        when {
            fillNeedsRebuild || strokeNeedsRebuild ->
                recolorImageVector(imageVector, tint, tintCap, tintStroke, tintStrokeCap)
            else -> null
        }
    }
    val effectiveTint: Color = when {
        tintCap.isUndefined -> Color.Unspecified
        recolored != null -> Color.Unspecified
        else -> tint
    }
    val effectiveVector: ImageVector = recolored ?: imageVector

    MaterialIcon(
        imageVector = effectiveVector,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = effectiveTint
    )
}