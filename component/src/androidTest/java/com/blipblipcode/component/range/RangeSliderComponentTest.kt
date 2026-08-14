package com.blipblipcode.component.range

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.blipblipcode.component.slider.SliderDefaults
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented UI tests for [RangeSliderComponent].
 *
 * The component renders a Material 3 RangeSlider with a custom track (Canvas). For an
 * active range of `0.2f..0.8f`, the inactive track fills `0..0.2` and `0.8..1.0`, while
 * the active track fills `0.2..0.8`. Pixel sampling at the vertical centre of the slider
 * proves these proportions are honoured with the configured colours.
 */
class RangeSliderComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testTagValue = "range-slider-under-test"

    private val activeColor = Color(0xFFD32F2F)    // red
    private val inactiveColor = Color(0xFF1976D2)  // blue

    private val sliderWidthDp = 240.dp
    private val sliderHeightDp = 48.dp

    @OptIn(ExperimentalMaterial3Api::class)
    private fun render(start: Float, end: Float): IntArray {
        composeTestRule.setContent {
            val state = remember {
                RangeSliderState(
                    activeRangeStart = start,
                    activeRangeEnd = end,
                    steps = 0,
                    valueRange = 0f..1f,
                )
            }
            Box(
                modifier = Modifier
                    .size(sliderWidthDp, sliderHeightDp)
                    .background(Color.White)
                    .testTag(testTagValue)
            ) {
                RangeSliderComponent(
                    state = state,
                    modifier = Modifier.size(sliderWidthDp, sliderHeightDp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = activeColor,
                        inactiveTrackColor = inactiveColor,
                    ),
                )
            }
        }
        composeTestRule.waitForIdle()
        val bmp = composeTestRule.onNodeWithTag(testTagValue).captureToImage().asAndroidBitmap()
        val w = bmp.width
        val h = bmp.height
        return intArrayOf(
            bmp.getPixel(w * 10 / 100, h / 2),  // x = 10% — should be inactive (left of range)
            bmp.getPixel(w * 50 / 100, h / 2),  // x = 50% — should be active (middle of range)
            bmp.getPixel(w * 90 / 100, h / 2)   // x = 90% — should be inactive (right of range)
        )
    }

    @Test
    fun active_range_middle_is_active_colour() {
        val px = render(start = 0.2f, end = 0.8f)
        assertEquals(activeColor.toArgb(), px[1])
    }

    @Test
    fun outside_active_range_left_is_inactive_colour() {
        val px = render(start = 0.2f, end = 0.8f)
        assertEquals(inactiveColor.toArgb(), px[0])
    }

    @Test
    fun outside_active_range_right_is_inactive_colour() {
        val px = render(start = 0.2f, end = 0.8f)
        assertEquals(inactiveColor.toArgb(), px[2])
    }

    @Test
    fun full_range_paints_only_active_colour() {
        val px = render(start = 0f, end = 1f)
        assertEquals(activeColor.toArgb(),   px[0])
        assertEquals(activeColor.toArgb(),   px[1])
        assertEquals(activeColor.toArgb(),   px[2])
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun empty_range_paints_only_inactive_colour() {
        // activeRangeStart == activeRangeEnd → no active fill, everything is the inactive
        // track. We sample at x = 25% and x = 75% to stay clear of the rounded end-cap
        // that gets drawn at the start/end position (which is the active colour).
        composeTestRule.setContent {
            val state = remember {
                RangeSliderState(
                    activeRangeStart = 0.2f,
                    activeRangeEnd = 0.2f,
                    steps = 0,
                    valueRange = 0f..1f,
                )
            }
            Box(
                modifier = Modifier
                    .size(sliderWidthDp, sliderHeightDp)
                    .background(Color.White)
                    .testTag(testTagValue)
            ) {
                RangeSliderComponent(
                    state = state,
                    modifier = Modifier.size(sliderWidthDp, sliderHeightDp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = activeColor,
                        inactiveTrackColor = inactiveColor,
                    ),
                )
            }
        }
        composeTestRule.waitForIdle()
        val bmp = composeTestRule.onNodeWithTag(testTagValue).captureToImage().asAndroidBitmap()
        val px = intArrayOf(
            bmp.getPixel(bmp.width * 25 / 100, bmp.height / 2),
            bmp.getPixel(bmp.width * 50 / 100, bmp.height / 2),
            bmp.getPixel(bmp.width * 75 / 100, bmp.height / 2),
        )
        assertEquals(inactiveColor.toArgb(), px[0])
        assertEquals(inactiveColor.toArgb(), px[1])
        assertEquals(inactiveColor.toArgb(), px[2])
    }
}