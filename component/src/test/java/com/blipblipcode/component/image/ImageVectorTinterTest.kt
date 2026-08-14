package com.blipblipcode.component.image

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorNode
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Verifies the internal recolorImageVector function preserves the structure of the source
 * ImageVector and applies the tint only to the requested top-level layers (both for fill
 * via [tintCap] and for stroke via [tintStrokeCap]).
 */
class ImageVectorTinterTest {

    /**
     * Builds a 3-layer ImageVector where each top-level layer is a single path filled with
     * a different distinctive color and stroked with a different distinctive color.
     */
    private fun threeLayerVector(): ImageVector {
        val builder = ImageVector.Builder(
            name = "three-layer",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        )
        builder.addPath(
            pathData = emptyList(),
            name = "p0",
            fill = SolidColor(Color.Red),
            stroke = SolidColor(Color(0xFF800000))
        )
        builder.addPath(
            pathData = emptyList(),
            name = "p1",
            fill = SolidColor(Color.Green),
            stroke = SolidColor(Color(0xFF008000))
        )
        builder.addPath(
            pathData = emptyList(),
            name = "p2",
            fill = SolidColor(Color.Blue),
            stroke = SolidColor(Color(0xFF000080))
        )
        return builder.build()
    }

    private fun topLevelPathsOf(vector: ImageVector): List<VectorPath> {
        val out = ArrayList<VectorPath>()
        for (node: VectorNode in vector.root) {
            if (node is VectorPath) out += node
        }
        return out
    }

    // --- Fill tinting (legacy behaviour) -------------------------------------------

    @Test
    fun `Undefined returns the same source untouched`() {
        val source = threeLayerVector()
        val result = recolorImageVector(source, Color.Magenta, TintCap.Undefined)
        assertSame(source, result)
    }

    @Test
    fun `All rebuilds the vector with every layer fill tinted`() {
        val source = threeLayerVector()
        val result = recolorImageVector(source, Color.Magenta, TintCap.All)
        val paths = topLevelPathsOf(result)
        assertEquals(3, paths.size)
        paths.forEach { p ->
            assertEquals("fill must be SolidColor", true, p.fill is SolidColor)
            assertEquals(Color.Magenta, (p.fill as SolidColor).value)
        }
    }

    @Test
    fun `Index tints only the matching layer fill and preserves the others`() {
        val source = threeLayerVector()
        val result = recolorImageVector(source, Color.Magenta, TintCap.index(1))
        val paths = topLevelPathsOf(result)
        assertEquals(3, paths.size)
        assertEquals(Color.Red, (paths[0].fill as SolidColor).value)
        assertEquals(Color.Magenta, (paths[1].fill as SolidColor).value)
        assertEquals(Color.Blue, (paths[2].fill as SolidColor).value)
    }

    @Test
    fun `Range tints every layer fill inside the range`() {
        val source = threeLayerVector()
        val result = recolorImageVector(source, Color.Magenta, TintCap.range(0..1))
        val paths = topLevelPathsOf(result)
        assertEquals(Color.Magenta, (paths[0].fill as SolidColor).value)
        assertEquals(Color.Magenta, (paths[1].fill as SolidColor).value)
        assertEquals(Color.Blue, (paths[2].fill as SolidColor).value)
    }

    @Test
    fun `Layers tints only the specified positions fill`() {
        val source = threeLayerVector()
        val result = recolorImageVector(source, Color.Magenta, TintCap.layers(0, 2))
        val paths = topLevelPathsOf(result)
        assertEquals(Color.Magenta, (paths[0].fill as SolidColor).value)
        assertEquals(Color.Green, (paths[1].fill as SolidColor).value)
        assertEquals(Color.Magenta, (paths[2].fill as SolidColor).value)
    }

    // --- Stroke tinting -----------------------------------------------------------

    @Test
    fun `stroke null leaves strokes untouched when fill is All`() {
        val source = threeLayerVector()
        val result = recolorImageVector(source, Color.Magenta, TintCap.All)
        val paths = topLevelPathsOf(result)
        assertEquals(Color(0xFF800000), (paths[0].stroke as SolidColor).value)
        assertEquals(Color(0xFF008000), (paths[1].stroke as SolidColor).value)
        assertEquals(Color(0xFF000080), (paths[2].stroke as SolidColor).value)
    }

    @Test
    fun `stroke All recolors every layer stroke when fill is undefined`() {
        val source = threeLayerVector()
        val result = recolorImageVector(
            source = source,
            tint = Color.Black,
            tintCap = TintCap.Undefined,
            strokeTint = Color.Cyan,
            strokeTintCap = TintStroke.All
        )
        assertNotSame(source, result)
        val paths = topLevelPathsOf(result)
        assertEquals(Color.Cyan, (paths[0].stroke as SolidColor).value)
        assertEquals(Color.Cyan, (paths[1].stroke as SolidColor).value)
        assertEquals(Color.Cyan, (paths[2].stroke as SolidColor).value)
        // Fill untouched
        assertEquals(Color.Red, (paths[0].fill as SolidColor).value)
        assertEquals(Color.Green, (paths[1].fill as SolidColor).value)
        assertEquals(Color.Blue, (paths[2].fill as SolidColor).value)
    }

    @Test
    fun `stroke Index recolors only the matching layer stroke`() {
        val source = threeLayerVector()
        val result = recolorImageVector(
            source = source,
            tint = Color.Black,
            tintCap = TintCap.Undefined,
            strokeTint = Color.Cyan,
            strokeTintCap = TintStroke.index(1)
        )
        val paths = topLevelPathsOf(result)
        assertEquals(Color(0xFF800000), (paths[0].stroke as SolidColor).value)
        assertEquals(Color.Cyan, (paths[1].stroke as SolidColor).value)
        assertEquals(Color(0xFF000080), (paths[2].stroke as SolidColor).value)
    }

    @Test
    fun `stroke Range recolors every layer stroke inside the range`() {
        val source = threeLayerVector()
        val result = recolorImageVector(
            source = source,
            tint = Color.Black,
            tintCap = TintCap.Undefined,
            strokeTint = Color.Cyan,
            strokeTintCap = TintStroke.range(0..1)
        )
        val paths = topLevelPathsOf(result)
        assertEquals(Color.Cyan, (paths[0].stroke as SolidColor).value)
        assertEquals(Color.Cyan, (paths[1].stroke as SolidColor).value)
        assertEquals(Color(0xFF000080), (paths[2].stroke as SolidColor).value)
    }

    @Test
    fun `stroke Layers recolors only the specified positions`() {
        val source = threeLayerVector()
        val result = recolorImageVector(
            source = source,
            tint = Color.Black,
            tintCap = TintCap.Undefined,
            strokeTint = Color.Cyan,
            strokeTintCap = TintStroke.layers(0, 2)
        )
        val paths = topLevelPathsOf(result)
        assertEquals(Color.Cyan, (paths[0].stroke as SolidColor).value)
        assertEquals(Color(0xFF008000), (paths[1].stroke as SolidColor).value)
        assertEquals(Color.Cyan, (paths[2].stroke as SolidColor).value)
    }

    @Test
    fun `Undefined stroke cap leaves strokes untouched even when strokeTint is provided`() {
        val source = threeLayerVector()
        val result = recolorImageVector(
            source = source,
            tint = Color.Black,
            tintCap = TintCap.Undefined,
            strokeTint = Color.Cyan,
            strokeTintCap = TintStroke.Undefined
        )
        val paths = topLevelPathsOf(result)
        assertEquals(Color(0xFF800000), (paths[0].stroke as SolidColor).value)
        assertEquals(Color(0xFF008000), (paths[1].stroke as SolidColor).value)
        assertEquals(Color(0xFF000080), (paths[2].stroke as SolidColor).value)
    }

    @Test
    fun `fill and stroke can be recolored independently in the same call`() {
        val source = threeLayerVector()
        val result = recolorImageVector(
            source = source,
            tint = Color.Yellow,
            tintCap = TintCap.index(0),
            strokeTint = Color.Cyan,
            strokeTintCap = TintStroke.index(1)
        )
        val paths = topLevelPathsOf(result)
        // Fill only on layer 0
        assertEquals(Color.Yellow, (paths[0].fill as SolidColor).value)
        assertEquals(Color.Green, (paths[1].fill as SolidColor).value)
        assertEquals(Color.Blue, (paths[2].fill as SolidColor).value)
        // Stroke only on layer 1
        assertEquals(Color(0xFF800000), (paths[0].stroke as SolidColor).value)
        assertEquals(Color.Cyan, (paths[1].stroke as SolidColor).value)
        assertEquals(Color(0xFF000080), (paths[2].stroke as SolidColor).value)
    }

    // --- Structural preservation --------------------------------------------------

    @Test
    fun `recoloring produces a fresh ImageVector instance`() {
        val source = threeLayerVector()
        val result = recolorImageVector(source, Color.Magenta, TintCap.All)
        assertNotSame(source, result)
    }

    @Test
    fun `recoloring preserves viewport dimensions and name`() {
        val source = threeLayerVector()
        val result = recolorImageVector(source, Color.Magenta, TintCap.All)
        assertEquals(source.name, result.name)
        assertEquals(Dp(24f), result.defaultWidth)
        assertEquals(Dp(24f), result.defaultHeight)
        assertEquals(source.viewportWidth, result.viewportWidth, 0f)
        assertEquals(source.viewportHeight, result.viewportHeight, 0f)
        // Sanity: result must have a populated root
        assertNotNull(result.root)
        assertTrue(result.root.size >= 3)
    }
}