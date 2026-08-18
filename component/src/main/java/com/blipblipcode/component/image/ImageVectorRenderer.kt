package com.blipblipcode.component.image

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable

/**
 * Returns an [ImageVector] equivalent to [imageVector] with the fill recoloured by [tint] on
 * the layers matched by [tintCap] and the stroke recoloured by [tintStroke] on the layers
 * matched by [tintStrokeCap]. Layers (or strokes) that do not match keep their original
 * colours.
 *
 * The recoloured vector is cached in the composition and is rebuilt only when one of the
 * inputs changes.
 *
 * @see recolorImageVector for the rules applied to each [TintCap] / [TintStroke] variant.
 */
@Composable
fun rememberRecoloredImageVector(
    imageVector: ImageVector,
    tint: Color = Color.Unspecified,
    tintCap: TintCap = TintCap.Undefined,
    tintStroke: Color? = null,
    tintStrokeCap: TintStroke = TintStroke.All,
): ImageVector = remember(imageVector, tint, tintCap, tintStroke, tintStrokeCap) {
    recolorImageVector(
        source = imageVector,
        tint = tint,
        tintCap = tintCap,
        strokeTint = tintStroke,
        strokeTintCap = tintStrokeCap
    )
}

/**
 * Renders [imageVector] into a [Drawable] (a [BitmapDrawable] backed by an ARGB_8888 bitmap)
 * sized [widthDp] × [heightDp] in density-independent pixels, applying the same tint rules as
 * [rememberRecoloredImageVector].
 *
 * The bitmap is created lazily the first time the composition is reached and is rebuilt only
 * when one of the inputs changes.
 *
 * @param widthDp width of the resulting drawable, in dp.
 * @param heightDp height of the resulting drawable, in dp.
 */
@Composable
fun rememberImageVectorAsDrawable(
    imageVector: ImageVector,
    tint: Color = Color.Unspecified,
    tintCap: TintCap = TintCap.Undefined,
    tintStroke: Color? = null,
    tintStrokeCap: TintStroke = TintStroke.All,
    widthDp: Int = 96,
    heightDp: Int = 96,
): Drawable {
    val density = LocalDensity.current
    val resources = LocalResources.current
    val effectiveVector = rememberRecoloredImageVector(
        imageVector = imageVector,
        tint = tint,
        tintCap = tintCap,
        tintStroke = tintStroke,
        tintStrokeCap = tintStrokeCap
    )
    val painter = rememberVectorPainter(effectiveVector)

    val bitmap = remember(effectiveVector, density, widthDp, heightDp) {
        val widthPx = with(density) { widthDp.dp.toPx().toInt().coerceAtLeast(1) }
        val heightPx = with(density) { heightDp.dp.toPx().toInt().coerceAtLeast(1) }
        val androidBitmap = createBitmap(widthPx, heightPx)
        val imageBitmap = androidBitmap.asImageBitmap()
        val canvas = androidx.compose.ui.graphics.Canvas(imageBitmap)
        val size = Size(widthPx.toFloat(), heightPx.toFloat())
        val drawScope = CanvasDrawScope()

        drawScope.draw(
            density = density,
            layoutDirection = LayoutDirection.Ltr,
            canvas = canvas,
            size = size
        ) {
            with(painter) {
                draw(size)
            }
        }
        androidBitmap
    }

    return remember(bitmap) { bitmap.toDrawable(resources) }
}
