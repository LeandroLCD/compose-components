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
 * Instrumented UI tests for [ImageComponents] with the [tintStroke] parameter using the
 * [Icons.MapTruck] fixture. Mirrors [IconTintStrokeTest] for the Image composable.
 */
class ImageTintStrokeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testTagValue = "image-stroke-under-test"
    private val imageSizeDp = 128.dp

    private val strokeTintColor = Color(0xFFFF00FF) // magenta

    private val wheelsStrokeColor = Color(0xFF212121)
    private val bodyStrokeColor = Color(0xFFB71C1C)
    private val cabStrokeColor = Color(0xFF0D47A1)
    private val cargoStrokeColor = Color(0xFF1B5E20)

    private fun renderAndSampleStroke(
        tint: Color? = null,
        tintCap: TintCap = TintCap.Undefined,
        strokeTint: Color? = strokeTintColor,
        strokeCap: TintStroke = TintStroke.All,
    ): IntArray {
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .size(imageSizeDp)
                    .background(Color.White)
                    .testTag(testTagValue)
            ) {
                ImageComponents(
                    imageVector = Icons.MapTruck,
                    contentDescription = null,
                    modifier = Modifier.size(imageSizeDp),
                    tint = tint,
                    tintCap = tintCap,
                    tintStroke = strokeTint,
                    tintStrokeCap = strokeCap,
                )
            }
        }
        composeTestRule.waitForIdle()
        val bmp = composeTestRule.onNodeWithTag(testTagValue).captureToImage().asAndroidBitmap()
        val w = bmp.width
        val h = bmp.height
        return intArrayOf(
            bmp.getPixel(w * 17 / 64, h * 54 / 64), // right edge of front tire (wheels stroke)
            bmp.getPixel(w * 32 / 64, h * 44 / 64), // top edge of body chassis
            bmp.getPixel(w * 51 / 64, h * 14 / 64), // top edge of cab shell
            bmp.getPixel(w * 20 / 64, h * 4 / 64)   // top edge of cargo
        )
    }

    @Test
    fun stroke_null_preserves_original_stroke_colors() {
        val px = renderAndSampleStroke(strokeTint = null)
        assertEquals(wheelsStrokeColor.toArgb(), px[0])
        assertEquals(bodyStrokeColor.toArgb(), px[1])
        assertEquals(cabStrokeColor.toArgb(), px[2])
        assertEquals(cargoStrokeColor.toArgb(), px[3])
    }

    @Test
    fun stroke_all_recolors_every_layer_stroke() {
        val px = renderAndSampleStroke(strokeCap = TintStroke.All)
        assertEquals(strokeTintColor.toArgb(), px[0])
        assertEquals(strokeTintColor.toArgb(), px[1])
        assertEquals(strokeTintColor.toArgb(), px[2])
        assertEquals(strokeTintColor.toArgb(), px[3])
    }

    @Test
    fun stroke_index_recolors_only_the_matching_layer_stroke() {
        val px = renderAndSampleStroke(strokeCap = TintStroke.index(2))
        assertEquals(wheelsStrokeColor.toArgb(), px[0])
        assertEquals(bodyStrokeColor.toArgb(), px[1])
        assertEquals(strokeTintColor.toArgb(), px[2])
        assertEquals(cargoStrokeColor.toArgb(), px[3])
    }

    @Test
    fun stroke_range_recolors_only_layers_inside_the_range() {
        val px = renderAndSampleStroke(strokeCap = TintStroke.range(0..1))
        assertEquals(strokeTintColor.toArgb(), px[0])
        assertEquals(strokeTintColor.toArgb(), px[1])
        assertEquals(cabStrokeColor.toArgb(), px[2])
        assertEquals(cargoStrokeColor.toArgb(), px[3])
    }

    @Test
    fun stroke_layers_recolors_only_the_specified_positions() {
        val px = renderAndSampleStroke(strokeCap = TintStroke.layers(0, 3))
        assertEquals(strokeTintColor.toArgb(), px[0])
        assertEquals(bodyStrokeColor.toArgb(), px[1])
        assertEquals(cabStrokeColor.toArgb(), px[2])
        assertEquals(strokeTintColor.toArgb(), px[3])
    }

    @Test
    fun stroke_undefined_preserves_original_stroke_colors_even_when_strokeTint_is_set() {
        val px = renderAndSampleStroke(strokeCap = TintStroke.Undefined)
        assertEquals(wheelsStrokeColor.toArgb(), px[0])
        assertEquals(bodyStrokeColor.toArgb(), px[1])
        assertEquals(cabStrokeColor.toArgb(), px[2])
        assertEquals(cargoStrokeColor.toArgb(), px[3])
    }
}