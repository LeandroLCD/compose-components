package com.blipblipcode.component.image

import androidx.compose.material3.Icon as MaterialIcon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * A thin wrapper around Material 3's [MaterialIcon] that adds [tintCap] support for vector
 * drawables. [tintCap] controls which layers of the [imageVector] receive the [tint] color;
 * the rest are rendered with their original colors.
 *
 * @see TintCap
 */
@Composable
fun IconComponents(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    tintCap: TintCap = TintCap.All,
) {
    val recolored: ImageVector? = remember(imageVector, tint, tintCap) {
        when {
            tintCap.isUndefined -> null
            tintCap === TintCap.All -> null
            else -> recolorImageVector(imageVector, tint, tintCap)
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