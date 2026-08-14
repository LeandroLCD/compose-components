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
 * Top-level layers (indices) are drawn on non-overlapping regions so each one can be
 * pixel-tested in isolation:
 *   0 → `wheels` (group containing both tires, bottom strip)
 *   1 → `body`   (narrow chassis strip above the wheels)
 *   2 → `cab`    (driver cabin shell + window, top-right)
 *   3 → `cargo`  (cargo box, top-left)
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
            // Front tire (bottom-left, fully visible below the chassis)
            addPath(
                pathData = tirePath(cx = 12f, cy = 54f, r = 5f),
                name = "tire-front",
                fill = SolidColor(Color(0xFF424242)),
                fillAlpha = 1f,
                stroke = SolidColor(Color(0xFF212121)),
                strokeAlpha = 1f,
                strokeLineWidth = 1.5f
            )
            // Rear tire (bottom-right, fully visible below the chassis)
            addPath(
                pathData = tirePath(cx = 50f, cy = 54f, r = 5f),
                name = "tire-rear",
                fill = SolidColor(Color(0xFF424242)),
                fillAlpha = 1f,
                stroke = SolidColor(Color(0xFF212121)),
                strokeAlpha = 1f,
                strokeLineWidth = 1.5f
            )
        }

        // Layer 1: narrow chassis strip (full width, sits between the wheels and the cab/cargo)
        addPath(
            pathData = listOf(
                PathNode.MoveTo(2f, 44f),
                PathNode.LineTo(62f, 44f),
                PathNode.LineTo(62f, 48f),
                PathNode.LineTo(2f, 48f),
                PathNode.Close
            ),
            name = "body",
            fill = SolidColor(Color(0xFFE53935)),
            fillAlpha = 1f
        )

        // Layer 2: driver cabin (group: cabin shell + window, both tinted together)
        // Positioned in the top-right region (no overlap with cargo).
        group(
            name = "cab",
            rotate = 0f,
            pivotX = 0f,
            pivotY = 0f,
            scaleX = 1f,
            scaleY = 1f,
            translationX = 0f,
            translationY = 0f,
            clipPathData = emptyList()
        ) {
            addPath(
                pathData = listOf(
                    PathNode.MoveTo(40f, 14f),
                    PathNode.LineTo(62f, 14f),
                    PathNode.LineTo(62f, 42f),
                    PathNode.LineTo(40f, 42f),
                    PathNode.Close
                ),
                name = "cab-shell",
                fill = SolidColor(Color(0xFF1E88E5)),
                fillAlpha = 1f
            )
            addPath(
                pathData = listOf(
                    PathNode.MoveTo(44f, 18f),
                    PathNode.LineTo(58f, 18f),
                    PathNode.LineTo(58f, 26f),
                    PathNode.LineTo(44f, 26f),
                    PathNode.Close
                ),
                name = "cab-window",
                fill = SolidColor(Color(0xFFBBDEFB)),
                fillAlpha = 1f
            )
        }

        // Layer 3: cargo box (top-left region)
        addPath(
            pathData = listOf(
                PathNode.MoveTo(2f, 4f),
                PathNode.LineTo(38f, 4f),
                PathNode.LineTo(38f, 42f),
                PathNode.LineTo(2f, 42f),
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
