package com.blipblipcode.component.image

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorGroup
import androidx.compose.ui.graphics.vector.VectorNode
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.graphics.vector.group

/**
 * Rebuilds [source] into a new [ImageVector] applying [tint] only to the fill of the layers
 * matched by [tintCap], and [strokeTint] only to the stroke of the layers matched by
 * [strokeTintCap]. Layers (or strokes) that do not match keep their original colors.
 *
 * Fill rules:
 * - When [tintCap] is [TintCap.Undefined] the fill is left untouched.
 * - When [tintCap] is [TintCap.All] every layer's fill is set to [tint].
 * - Otherwise only the matching layers' fills are recolored.
 *
 * Stroke rules:
 * - When [strokeTint] is `null` the stroke is left untouched.
 * - When [strokeTint] is non-null and [strokeTintCap] is [TintStroke.All] every layer's stroke
 *   is set to [strokeTint].
 * - When [strokeTint] is non-null and [strokeTintCap] is [TintStroke.Undefined] the stroke is
 *   left untouched.
 * - Otherwise only the matching layers' strokes are recolored.
 */
internal fun recolorImageVector(
    source: ImageVector,
    tint: Color,
    tintCap: TintCap,
    strokeTint: Color? = null,
    strokeTintCap: TintStroke = TintStroke.All,
): ImageVector {
    if (tintCap.isUndefined && (strokeTint == null || strokeTintCap.isUndefined)) {
        return source
    }

    val builder = ImageVector.Builder(
        name = source.name,
        defaultWidth = source.defaultWidth,
        defaultHeight = source.defaultHeight,
        viewportWidth = source.viewportWidth,
        viewportHeight = source.viewportHeight
    )

    val tintBrush: Brush = SolidColor(tint)
    val strokeBrush: Brush? = strokeTint?.let { SolidColor(it) }
    val recolorFill = !tintCap.isUndefined
    val recolorStroke = strokeBrush != null && !strokeTintCap.isUndefined

    // Top-level nodes form the layer-index space. Iterate as a snapshot to be safe.
    val topLevel = source.root.toNodeList()
    topLevel.forEachIndexed { index, node ->
        val shouldTintFill = recolorFill && tintCap.appliesTo(index)
        val shouldTintStroke = recolorStroke && strokeTintCap.appliesTo(index)
        copyNode(
            builder = builder,
            node = node,
            tintBrush = tintBrush,
            shouldTintFill = shouldTintFill,
            strokeBrush = strokeBrush,
            shouldTintStroke = shouldTintStroke
        )
    }

    return builder.build()
}

private fun copyNode(
    builder: ImageVector.Builder,
    node: VectorNode,
    tintBrush: Brush,
    shouldTintFill: Boolean,
    strokeBrush: Brush?,
    shouldTintStroke: Boolean,
) {
    when (node) {
        is VectorGroup -> copyGroupInto(
            builder = builder,
            sourceGroup = node,
            tintBrush = tintBrush,
            shouldTintFill = shouldTintFill,
            strokeBrush = strokeBrush,
            shouldTintStroke = shouldTintStroke
        )
        is VectorPath -> copyPathInto(
            builder = builder,
            sourcePath = node,
            tintBrush = tintBrush,
            shouldTintFill = shouldTintFill,
            strokeBrush = strokeBrush,
            shouldTintStroke = shouldTintStroke
        )
    }
}

private fun copyGroupInto(
    builder: ImageVector.Builder,
    sourceGroup: VectorGroup,
    tintBrush: Brush,
    shouldTintFill: Boolean,
    strokeBrush: Brush?,
    shouldTintStroke: Boolean,
) {
    builder.group(
        name = sourceGroup.name,
        rotate = sourceGroup.rotation,
        pivotX = sourceGroup.pivotX,
        pivotY = sourceGroup.pivotY,
        scaleX = sourceGroup.scaleX,
        scaleY = sourceGroup.scaleY,
        translationX = sourceGroup.translationX,
        translationY = sourceGroup.translationY,
        clipPathData = sourceGroup.clipPathData
    ) {
        val children = sourceGroup.toNodeList()
        children.forEach { child ->
            copyNode(
                builder = this,
                node = child,
                tintBrush = tintBrush,
                shouldTintFill = shouldTintFill,
                strokeBrush = strokeBrush,
                shouldTintStroke = shouldTintStroke
            )
        }
    }
}

private fun copyPathInto(
    builder: ImageVector.Builder,
    sourcePath: VectorPath,
    tintBrush: Brush,
    shouldTintFill: Boolean,
    strokeBrush: Brush?,
    shouldTintStroke: Boolean,
) {
    builder.addPath(
        pathData = sourcePath.pathData,
        pathFillType = sourcePath.pathFillType,
        name = sourcePath.name,
        fill = if (shouldTintFill) tintBrush else sourcePath.fill,
        fillAlpha = sourcePath.fillAlpha,
        stroke = if (shouldTintStroke) strokeBrush ?: sourcePath.stroke else sourcePath.stroke,
        strokeAlpha = sourcePath.strokeAlpha,
        strokeLineWidth = sourcePath.strokeLineWidth,
        strokeLineCap = sourcePath.strokeLineCap,
        strokeLineJoin = sourcePath.strokeLineJoin,
        strokeLineMiter = sourcePath.strokeLineMiter,
        trimPathStart = sourcePath.trimPathStart,
        trimPathEnd = sourcePath.trimPathEnd,
        trimPathOffset = sourcePath.trimPathOffset
    )
}

/** Snapshot helper for any [VectorGroup] iterable. */
private fun VectorGroup.toNodeList(): List<VectorNode> {
    val out = ArrayList<VectorNode>(size)
    val it = iterator()
    while (it.hasNext()) out += it.next()
    return out
}