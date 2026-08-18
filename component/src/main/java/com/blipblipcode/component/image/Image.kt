package com.blipblipcode.component.image

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image as FoundationImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale

/**
 * A wrapper around Compose Foundation's [FoundationImage] that adds [tintCap] and
 * [tintStroke] support for vector drawables.
 *
 * - [tintCap] controls which layers of the [imageVector] receive the fill [tint] color; the
 *   rest are rendered with their original fill colors.
 * - [tintStroke] is the optional color used to recolor the stroke of the layers selected by
 *   [tintStrokeCap]. When `null` (default) the vector's original strokes are preserved.
 *
 * Fill behavior:
 * - When [tint] is `null` no fill tint is applied (standard behavior).
 * - When [tint] is non-null and [tintCap] is [TintCap.Undefined], the tint is ignored and the
 *   vector's original fill colors are preserved.
 * - When [tint] is non-null and [tintCap] is [TintCap.All], the fill tint is applied to every
 *   layer using [ColorFilter.tint].
 * - When [tint] is non-null and [tintCap] is [TintCap.Index], [TintCap.Range] or
 *   [TintCap.Layers], the vector is rebuilt so only the matching layers are tinted and
 *   [ColorFilter] is left untouched.
 *
 * Stroke behavior:
 * - When [tintStroke] is `null`, the vector's original strokes are preserved.
 * - When [tintStroke] is non-null the vector is rebuilt so the strokes of the layers matched by
 *   [tintStrokeCap] are recolored.
 */
@Composable
fun ImageComponents(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    tint: Color? = null,
    tintCap: TintCap = TintCap.Undefined,
    tintStroke: Color? = null,
    tintStrokeCap: TintStroke = TintStroke.All,
) {
    val effectiveTintColor = tint ?: Color.Unspecified
    val effectiveVector = rememberRecoloredImageVector(
        imageVector = imageVector,
        tint = effectiveTintColor,
        tintCap = tintCap,
        tintStroke = tintStroke,
        tintStrokeCap = tintStrokeCap
    )

    val effectiveColorFilter: ColorFilter? = when {
        tintStroke != null -> colorFilter
        tint == null -> colorFilter
        tintCap.isUndefined -> colorFilter
        tintCap === TintCap.All -> colorFilter ?: ColorFilter.tint(tint)
        else -> colorFilter
    }

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

/**
 * Convenience overload that renders [imageVector] into a [Drawable] (via
 * [rememberImageVectorAsDrawable]) and then displays it with Compose Foundation's
 * [FoundationImage].
 *
 * Use this when you need the image rasterised, e.g. when handing the bitmap off to APIs that
 * only accept [Drawable].
 */
@Composable
fun ImageComponents(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    tint: Color? = null,
    tintCap: TintCap = TintCap.Undefined,
    tintStroke: Color? = null,
    tintStrokeCap: TintStroke = TintStroke.All,
    widthDp: Int = 96,
    heightDp: Int = 96,
) {
    val effectiveTintColor = tint ?: Color.Unspecified
    val drawable: Drawable = rememberImageVectorAsDrawable(
        imageVector = imageVector,
        tint = effectiveTintColor,
        tintCap = tintCap,
        tintStroke = tintStroke,
        tintStrokeCap = tintStrokeCap,
        widthDp = widthDp,
        heightDp = heightDp
    )
    val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap
    val imageBitmap: ImageBitmap = bitmap.asImageBitmap()
    val effectiveColorFilter: ColorFilter? = when {
        colorFilter != null -> colorFilter
        tint != null && tintCap === TintCap.All -> ColorFilter.tint(tint)
        else -> null
    }
    FoundationImage(
        painter = BitmapPainter(imageBitmap),
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = effectiveColorFilter
    )
}
