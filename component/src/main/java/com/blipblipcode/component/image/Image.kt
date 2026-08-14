package com.blipblipcode.component.image

import androidx.compose.foundation.Image as FoundationImage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.DefaultAlpha

/**
 * A wrapper around Compose Foundation's [FoundationImage] that adds [tintCap] support for
 * vector drawables. [tintCap] controls which layers of the [imageVector] receive the [tint]
 * color; the rest are rendered with their original colors.
 *
 * - When [tint] is `null` no tint is applied (standard behavior).
 * - When [tint] is non-null and [tintCap] is [TintCap.Undefined], the tint is ignored and the
 *   vector's original colors are preserved.
 * - When [tint] is non-null and [tintCap] is [TintCap.All], the tint is applied to every
 *   layer using [ColorFilter.tint].
 * - When [tint] is non-null and [tintCap] is [TintCap.Index], [TintCap.Range] or
 *   [TintCap.Layers], the vector is rebuilt so only the matching layers are tinted and
 *   [ColorFilter] is left untouched.
 */
@Composable
fun Image(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    tint: Color? = null,
    tintCap: TintCap = TintCap.Undefined,
) {
    val recolored: ImageVector? = remember(imageVector, tint, tintCap) {
        if (tint == null || tintCap.isUndefined || tintCap === TintCap.All) {
            null
        } else {
            recolorImageVector(imageVector, tint, tintCap)
        }
    }
    val effectiveColorFilter: ColorFilter? = when {
        tint == null -> colorFilter
        tintCap.isUndefined -> colorFilter
        tintCap === TintCap.All -> colorFilter ?: ColorFilter.tint(tint)
        else -> colorFilter
    }
    val effectiveVector: ImageVector = recolored ?: imageVector

    FoundationImage(
        imageVector = effectiveVector,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = effectiveColorFilter
    )
}