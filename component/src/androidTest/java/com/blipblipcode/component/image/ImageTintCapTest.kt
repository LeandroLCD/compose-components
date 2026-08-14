package com.blipblipcode.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
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
 * Instrumented UI tests for [Image] with the various [TintCap] variants, using the
 * [Icons.MapTruck] fixture. Mirrors [IconTintCapTest] for the Image composable.
 *
 * Top-level layers: 0 → wheels (group), 1 → body, 2 → cab, 3 → cargo.
 */
class ImageTintCapTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testTagValue = "image-under-test"
    private val imageSizeDp = 128.dp

    private val tintColor = Color(0xFFFFEB3B) // yellow

    private val wheelsColor = Color(0xFF424242)
    private val bodyColor = Color(0xFFE53935)
    private val cabColor = Color(0xFF1E88E5)
    private val cargoColor = Color(0xFF43A047)

    private fun renderAndSample(cap: TintCap): IntArray {
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .size(imageSizeDp)
                    .background(Color.White)
                    .testTag(testTagValue)
            ) {
                Image(
                    imageVector = Icons.MapTruck,
                    contentDescription = null,
                    modifier = Modifier.size(imageSizeDp),
                    tint = tintColor,
                    tintCap = cap
                )
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
    fun undefined_with_tint_still_preserves_original_colors() {
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
}