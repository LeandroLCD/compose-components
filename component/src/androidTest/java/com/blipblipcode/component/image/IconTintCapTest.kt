package com.blipblipcode.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented UI tests for [Icon] with the various [TintCap] variants, using the
 * [Icons.MapTruck] fixture which contains 4 distinct top-level layers:
 *   0 → `wheels` (group), 1 → `body`, 2 → `cab` (cabin + window), 3 → `cargo`.
 *
 * Each test renders the truck inside an [Icon] with a specific [TintCap], captures the
 * resulting bitmap of a fixed-size [Box] that wraps the [Icon], and samples well-known
 * pixel coordinates to verify that only the layers targeted by [tintCap] receive the tint
 * colour while the rest keep their original colour.
 */
class IconTintCapTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testTagValue = "icon-under-test"

    // Render size: square so the 64x64 viewport maps cleanly to a square pixel buffer.
    private val iconSizeDp = 128.dp

    private val tintColor = Color(0xFFFFEB3B) // yellow

    // Default layer colours of Icons.MapTruck — see MapTruck.kt
    private val wheelsColor = Color(0xFF424242)
    private val bodyColor = Color(0xFFE53935)
    private val cabColor = Color(0xFF1E88E5)
    private val cargoColor = Color(0xFF43A047)

    /**
     * Samples the rendered icon at the centre of every layer. Returns an `IntArray` of
     * length 4 ordered as: [wheels, body, cab, cargo]. Coordinates are expressed as a
     * fraction of the rendered pixel buffer; with [iconSizeDp] = 128.dp and a 64x64
     * viewport, positions match the source coords * 2.
     */
    private fun renderAndSample(cap: TintCap): IntArray {
        composeTestRule.setContent {
            Surface(modifier = Modifier.background(Color.White)) {
                Box(
                    modifier = Modifier
                        .size(iconSizeDp)
                        .background(Color.White)
                        .testTag(testTagValue)
                ) {
                    Icon(
                        imageVector = Icons.MapTruck,
                        contentDescription = null,
                        modifier = Modifier.size(iconSizeDp),
                        tint = tintColor,
                        tintCap = cap
                    )
                }
            }
        }
        composeTestRule.waitForIdle()
        val bmp = composeTestRule.onNodeWithTag(testTagValue).captureToImage().asAndroidBitmap()
        val w = bmp.width
        val h = bmp.height
        return intArrayOf(
            bmp.getPixel(w * 12 / 64, h * 54 / 64),  // front tire (wheels group)
            bmp.getPixel(w * 32 / 64, h * 46 / 64),  // body chassis strip
            bmp.getPixel(w * 52 / 64, h * 32 / 64),  // cab shell (below window)
            bmp.getPixel(w * 20 / 64, h * 20 / 64)   // cargo box
        )
    }

    @Test
    fun undefined_preserves_every_layer_original_color() {
        val px = renderAndSample(TintCap.Undefined)
        assertEquals(wheelsColor.toArgb(), px[0])
        assertEquals(bodyColor.toArgb(), px[1])
        assertEquals(cabColor.toArgb(), px[2])
        assertEquals(cargoColor.toArgb(), px[3])
    }

    @Test
    fun all_paints_every_layer_with_tint() {
        val px = renderAndSample(TintCap.All)
        assertEquals(tintColor.toArgb(), px[0])
        assertEquals(tintColor.toArgb(), px[1])
        assertEquals(tintColor.toArgb(), px[2])
        assertEquals(tintColor.toArgb(), px[3])
    }

    @Test
    fun index_tints_only_the_target_layer() {
        val px = renderAndSample(TintCap.index(2))
        assertEquals(wheelsColor.toArgb(), px[0])
        assertEquals(bodyColor.toArgb(), px[1])
        assertEquals(tintColor.toArgb(), px[2])
        assertEquals(cargoColor.toArgb(), px[3])
    }

    @Test
    fun range_tints_only_layers_inside_the_range() {
        val px = renderAndSample(TintCap.range(0..1))
        assertEquals(tintColor.toArgb(), px[0])
        assertEquals(tintColor.toArgb(), px[1])
        assertEquals(cabColor.toArgb(), px[2])
        assertEquals(cargoColor.toArgb(), px[3])
    }

    @Test
    fun layers_tints_only_the_specified_positions() {
        val px = renderAndSample(TintCap.layers(0, 3))
        assertEquals(tintColor.toArgb(), px[0])
        assertEquals(bodyColor.toArgb(), px[1])
        assertEquals(cabColor.toArgb(), px[2])
        assertEquals(tintColor.toArgb(), px[3])
    }

    @Test
    fun out_of_range_index_preserves_original_colors() {
        val px = renderAndSample(TintCap.index(99))
        assertEquals(wheelsColor.toArgb(), px[0])
        assertEquals(bodyColor.toArgb(), px[1])
        assertEquals(cabColor.toArgb(), px[2])
        assertEquals(cargoColor.toArgb(), px[3])
    }
}