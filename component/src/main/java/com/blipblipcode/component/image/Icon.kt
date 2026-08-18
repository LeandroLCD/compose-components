package com.blipblipcode.component.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.material3.Icon as MaterialIcon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import android.graphics.drawable.BitmapDrawable

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
    val rebuiltVector = rememberRecoloredImageVector(
        imageVector = imageVector,
        tint = tint,
        tintCap = tintCap,
        tintStroke = tintStroke,
        tintStrokeCap = tintStrokeCap
    )

    val needsRebuild = !tintCap.isUndefined && tintCap !== TintCap.All ||
        tintStroke != null && !tintStrokeCap.isUndefined
    val effectiveVector: ImageVector = if (needsRebuild) rebuiltVector else imageVector
    val effectiveTint: Color = when {
        tintCap.isUndefined -> Color.Unspecified
        needsRebuild -> Color.Unspecified
        else -> tint
    }

    MaterialIcon(
        imageVector = effectiveVector,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = effectiveTint
    )
}

/**
 * Convenience overload that renders [imageVector] into a [Drawable] (via
 * [rememberImageVectorAsDrawable]) and then displays it with Material 3's [MaterialIcon].
 *
 * Use this when you need the icon rasterised, e.g. when handing the icon off to APIs that
 * only accept [Drawable].
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
    widthDp: Int = 24,
    heightDp: Int = 24,
) {
    val drawable: Drawable = rememberImageVectorAsDrawable(
        imageVector = imageVector,
        tint = tint,
        tintCap = tintCap,
        tintStroke = tintStroke,
        tintStrokeCap = tintStrokeCap,
        widthDp = widthDp,
        heightDp = heightDp
    )
    val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap
    val imageBitmap: ImageBitmap = bitmap.asImageBitmap()
    MaterialIcon(
        painter = BitmapPainter(imageBitmap),
        contentDescription = contentDescription,
        modifier = modifier,
        tint = if (tintCap.isUndefined) Color.Unspecified else tint
    )
}
