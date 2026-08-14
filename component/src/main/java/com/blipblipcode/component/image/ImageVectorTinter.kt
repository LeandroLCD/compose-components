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
 * Rebuilds [source] into a new [ImageVector] applying [tint] only to the layers matched by
 * [tintCap]. Layers that do not match keep their original colors.
 *
 * When [tintCap] is [TintCap.All] (the default for [Icon]) the source vector is returned
 * untouched and tinting is expected to be applied externally via the standard
 * `tint` parameter — this avoids rebuilding the vector when not needed.
 *
 * When [tintCap] is [TintCap.Undefined] (the default for [Image]) the source vector is
 * returned untouched and no tint is applied at any level.
 */
internal fun recolorImageVector(
    source: ImageVector,
    tint: Color,
    tintCap: TintCap
): ImageVector {
    if (tintCap.isUndefined) return source

    val builder = ImageVector.Builder(
        name = source.name,
        defaultWidth = source.defaultWidth,
        defaultHeight = source.defaultHeight,
        viewportWidth = source.viewportWidth,
        viewportHeight = source.viewportHeight
    )

    val tintBrush: Brush = SolidColor(tint)

    // Top-level nodes form the layer-index space. Iterate as a snapshot to be safe.
    val topLevel = source.root.toNodeList()
    topLevel.forEachIndexed { index, node ->
        val shouldTint = tintCap.appliesTo(index)
        copyNode(builder, node, tintBrush, shouldTint)
    }

    return builder.build()
}

private fun copyNode(
    builder: ImageVector.Builder,
    node: VectorNode,
    tintBrush: Brush,
    shouldTint: Boolean
) {
    when (node) {
        is VectorGroup -> copyGroupInto(builder, node, tintBrush, shouldTint)
        is VectorPath -> copyPathInto(builder, node, tintBrush, shouldTint)
    }
}

private fun copyGroupInto(
    builder: ImageVector.Builder,
    sourceGroup: VectorGroup,
    tintBrush: Brush,
    shouldTint: Boolean
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
            copyNode(this, child, tintBrush, shouldTint)
        }
    }
}

private fun copyPathInto(
    builder: ImageVector.Builder,
    sourcePath: VectorPath,
    tintBrush: Brush,
    shouldTint: Boolean
) {
    builder.addPath(
        pathData = sourcePath.pathData,
        pathFillType = sourcePath.pathFillType,
        name = sourcePath.name,
        fill = if (shouldTint) tintBrush else sourcePath.fill,
        fillAlpha = sourcePath.fillAlpha,
        stroke = if (shouldTint) tintBrush else sourcePath.stroke,
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