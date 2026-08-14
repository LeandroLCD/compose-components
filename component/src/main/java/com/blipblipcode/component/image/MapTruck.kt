package com.blipblipcode.component.image

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.unit.dp

/**
 * A multi-layer truck icon used as a UI-test fixture for [Icon] / [Image] with [TintCap].
 *
 * Top-level layers (indices):
 *   0 → `wheels` (group containing both tires)
 *   1 → `body`   (the truck bed / chassis)
 *   2 → `cab`    (the driver cabin + window)
 *   3 → `cargo`  (the cargo box on the bed)
 *
 * Each layer uses a distinctive default colour so it is easy to verify which layers get
 * tinted by each [TintCap] variant.
 */
val Icons.MapTruck: ImageVector
    get() = _MapTruck ?: ImageVector.Builder(
        name = "MapTruck",
        defaultWidth = 64.dp,
        defaultHeight = 64.dp,
        viewportWidth = 64f,
        viewportHeight = 64f
    ).apply {
        // Layer 0: wheels group (both tires inside one top-level group)
        group(
            name = "wheels",
            rotate = 0f,
            pivotX = 0f,
            pivotY = 0f,
            scaleX = 1f,
            scaleY = 1f,
            translationX = 0f,
            translationY = 0f,
            clipPathData = emptyList()
        ) {
            // Front tire (left side, bottom)
            addPath(
                pathData = tirePath(cx = 14f, cy = 50f, r = 6f),
                name = "tire-front",
                fill = SolidColor(Color(0xFF424242)),
                fillAlpha = 1f,
                stroke = SolidColor(Color(0xFF212121)),
                strokeAlpha = 1f,
                strokeLineWidth = 1.5f
            )
            // Rear tire (right side, bottom)
            addPath(
                pathData = tirePath(cx = 50f, cy = 50f, r = 6f),
                name = "tire-rear",
                fill = SolidColor(Color(0xFF424242)),
                fillAlpha = 1f,
                stroke = SolidColor(Color(0xFF212121)),
                strokeAlpha = 1f,
                strokeLineWidth = 1.5f
            )
        }

        // Layer 1: truck bed / chassis
        addPath(
            pathData = listOf(
                PathNode.MoveTo(4f, 44f),
                PathNode.LineTo(40f, 44f),
                PathNode.LineTo(40f, 52f),
                PathNode.LineTo(4f, 52f),
                PathNode.Close
            ),
            name = "body",
            fill = SolidColor(Color(0xFFE53935)),
            fillAlpha = 1f
        )

        // Layer 2: driver cabin
        addPath(
            pathData = listOf(
                PathNode.MoveTo(40f, 24f),
                PathNode.LineTo(60f, 24f),
                PathNode.LineTo(60f, 52f),
                PathNode.LineTo(40f, 52f),
                PathNode.Close
            ),
            name = "cab",
            fill = SolidColor(Color(0xFF1E88E5)),
            fillAlpha = 1f
        )
        addPath(
            pathData = listOf(
                PathNode.MoveTo(44f, 28f),
                PathNode.LineTo(56f, 28f),
                PathNode.LineTo(56f, 38f),
                PathNode.LineTo(44f, 38f),
                PathNode.Close
            ),
            name = "window",
            fill = SolidColor(Color(0xFFBBDEFB)),
            fillAlpha = 1f
        )

        // Layer 3: cargo box
        addPath(
            pathData = listOf(
                PathNode.MoveTo(6f, 18f),
                PathNode.LineTo(38f, 18f),
                PathNode.LineTo(38f, 42f),
                PathNode.LineTo(6f, 42f),
                PathNode.Close
            ),
            name = "cargo",
            fill = SolidColor(Color(0xFF43A047)),
            fillAlpha = 1f
        )
    }.build().also { _MapTruck = it }

private var _MapTruck: ImageVector? = null

/**
 * Approximation of a circle centred at (cx, cy) with radius [r] using cubic bezier curves.
 * Sufficient for testing tint behaviour on filled regions.
 */
private fun tirePath(cx: Float, cy: Float, r: Float): List<PathNode> {
    val k = 0.5522847498f * r // standard circle-to-bezier constant
    return listOf(
        PathNode.MoveTo(cx + r, cy),
        PathNode.CurveTo(cx + r, cy + k, cx + k, cy + r, cx, cy + r),
        PathNode.CurveTo(cx - k, cy + r, cx - r, cy + k, cx - r, cy),
        PathNode.CurveTo(cx - r, cy - k, cx - k, cy - r, cx, cy - r),
        PathNode.CurveTo(cx + k, cy - r, cx + r, cy - k, cx + r, cy),
        PathNode.Close
    )
}

/**
 * Holder that mirrors the `androidx.compose.material.icons.Icons` style so consumers can
 * write `Icons.MapTruck` exactly like a Material icon.
 */
object Icons
