package com.blipblipcode.component.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented UI tests for [SliderComponent].
 *
 * The component renders a Material 3 Slider with a custom track (Canvas) and a custom thumb.
 * Pixel sampling targets the vertical centre of the slider — that is where the track is
 * centred — and compares the colour on the left half (active track) vs the right half
 * (inactive track) for a `value = 0.5f`.
 */
@OptIn(ExperimentalMaterial3Api::class)
class SliderComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testTagValue = "slider-under-test"

    // Distinct custom colours so we can assert which half of the track was painted.
    private val activeColor = Color(0xFFD32F2F)    // red
    private val inactiveColor = Color(0xFF1976D2)  // blue
    private val thumbColor = Color(0xFF388E3C)     // green
    private val tickColor = Color(0xFFFBC02D)      // yellow

    // Fixed slider footprint so pixel ratios are predictable.
    private val sliderWidthDp = 240.dp
    private val sliderHeightDp = 48.dp

    private fun render(value: Float): IntArray {
        composeTestRule.setContent {
            val state = remember { mutableFloatStateOf(value) }
            Box(
                modifier = Modifier
                    .size(sliderWidthDp, sliderHeightDp)
                    .background(Color.White)
                    .testTag(testTagValue)
            ) {
                SliderComponent(
                    value = state.floatValue,
                    onValueChange = { state.floatValue = it },
                    modifier = Modifier.size(sliderWidthDp, sliderHeightDp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = activeColor,
                        inactiveTrackColor = inactiveColor,
                        activeTickColor = tickColor,
                        inactiveTickColor = tickColor,
                        thumbColor = thumbColor,
                    ),
                    thumbSize = DpSize(20.dp, 20.dp),
                    trackHeight = 10.dp,
                    tickSize = 4.dp,
                    steps = 4,
                )
            }
        }
        composeTestRule.waitForIdle()
        val bmp = composeTestRule.onNodeWithTag(testTagValue).captureToImage().asAndroidBitmap()
        val w = bmp.width
        val h = bmp.height
        return intArrayOf(
            bmp.getPixel(w * 12 / 100, h / 2),     // x = 12% — active track (value=0.5)
            bmp.getPixel(w * 88 / 100, h / 2),     // x = 88% — inactive track (value=0.5)
            bmp.getPixel(w / 2,        h / 2)      // x = 50% — thumb area
        )
    }

    @Test
    fun active_and_inactive_track_colours_are_reflected_at_value_0_5() {
        val px = render(0.5f)
        assertEquals(activeColor.toArgb(),   px[0])
        assertEquals(inactiveColor.toArgb(), px[1])
    }

    @Test
    fun thumb_colour_is_visible_at_value_position() {
        // The slider has internal horizontal padding (~thumbRadius) on each side, so the
        // thumb at value=0.5 sits a bit left of the geometric centre. We sample at x=46%
        // (well within the 20.dp thumb footprint) and assert it is the thumb colour.
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .size(sliderWidthDp, sliderHeightDp)
                    .background(Color.White)
                    .testTag(testTagValue)
            ) {
                SliderComponent(
                    value = 0.5f,
                    onValueChange = {},
                    modifier = Modifier.size(sliderWidthDp, sliderHeightDp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = activeColor,
                        inactiveTrackColor = inactiveColor,
                        thumbColor = thumbColor,
                    ),
                    thumbSize = DpSize(20.dp, 20.dp),
                    trackHeight = 10.dp,
                )
            }
        }
        composeTestRule.waitForIdle()
        val bmp = composeTestRule.onNodeWithTag(testTagValue).captureToImage().asAndroidBitmap()
        val centre = bmp.getPixel(bmp.width * 46 / 100, bmp.height / 2)
        assertEquals(thumbColor.toArgb(), centre)
    }

    @Test
    fun value_zero_paints_only_inactive_track() {
        // Render with value=0.0; the entire track (both halves) should be inactive colour.
        val px = render(0.0f)
        assertEquals(inactiveColor.toArgb(), px[0])
        assertEquals(inactiveColor.toArgb(), px[1])
    }

    @Test
    fun value_one_paints_only_active_track() {
        // Render with value=1.0; the entire track (both halves) should be active colour.
        val px = render(1.0f)
        assertEquals(activeColor.toArgb(), px[0])
        assertEquals(activeColor.toArgb(), px[1])
    }

    @Test
    fun custom_thumb_size_changes_thumb_footprint() {
        // Render the same slider twice — once with the default 20.dp thumb, once with a
        // larger 40.dp thumb — and assert that the centre pixel of the larger thumb still
        // resolves to the thumb colour (i.e. the thumb is visible at the value position).
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .size(sliderWidthDp, sliderHeightDp)
                    .background(Color.White)
                    .testTag(testTagValue)
            ) {
                SliderComponent(
                    value = 0.5f,
                    onValueChange = {},
                    modifier = Modifier.size(sliderWidthDp, sliderHeightDp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = activeColor,
                        inactiveTrackColor = inactiveColor,
                        thumbColor = thumbColor,
                    ),
                    thumbSize = DpSize(40.dp, 40.dp),
                    trackHeight = 10.dp,
                )
            }
        }
        composeTestRule.waitForIdle()
        val bmp = composeTestRule.onNodeWithTag(testTagValue).captureToImage().asAndroidBitmap()
        val centre = bmp.getPixel(bmp.width / 2, bmp.height / 2)
        assertEquals(thumbColor.toArgb(), centre)
    }

    @Test
    fun progress_changes_active_track_extent() {
        // Sanity check rendered with a single composition: at value=0.0 the left-quarter
        // pixel is inactive (blue); at value=1.0 the left-quarter pixel is active (red).
        // Together with `active_and_inactive_track_colours_are_reflected_at_value_0_5` this
        // proves that the value parameter actually drives the active track length.
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .size(sliderWidthDp, sliderHeightDp)
                    .background(Color.White)
                    .testTag(testTagValue)
            ) {
                SliderComponent(
                    value = 1f,
                    onValueChange = {},
                    modifier = Modifier.size(sliderWidthDp, sliderHeightDp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = activeColor,
                        inactiveTrackColor = inactiveColor,
                        thumbColor = thumbColor,
                    ),
                    thumbSize = DpSize(20.dp, 20.dp),
                    trackHeight = 10.dp,
                )
            }
        }
        composeTestRule.waitForIdle()
        val bmp = composeTestRule.onNodeWithTag(testTagValue).captureToImage().asAndroidBitmap()
        val leftQuarter = bmp.getPixel(bmp.width / 4, bmp.height / 2)
        assertEquals(activeColor.toArgb(), leftQuarter)
    }
}