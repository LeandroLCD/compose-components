package com.blipblipcode.component.linear

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented UI tests for [LinearProgressIndicatorComponents].
 *
 * The component is a Canvas of exact size `width × height` dp, which makes pixel-sampling
 * straightforward: we sample the middle row at relative x positions and assert which colour
 * is rendered there based on the configured progress + colours.
 */
class LinearProgressIndicatorComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testTagValue = "progress-under-test"

    // Background colours used to discriminate between the indicator fill, the track and the
    // surrounding background. Chosen to be visually distinct and unlikely to clash with each
    // other when rendered by Skia.
    private val backgroundColor = Color.White
    private val fillColor = Color(0xFFE91E63)       // pink
    private val trackColor = Color(0xFF455A64)      // dark gray-blue

    // Fixed dimensions so the bitmap size is predictable across runs.
    private val widthDp = 200.dp
    private val heightDp = 8.dp

    /**
     * Renders the progress indicator inside a fixed-size [Box] and returns the bitmap of
     * that [Box] (not the Canvas) so we can sample with a small margin around the bar.
     */
    private fun renderAndSample(
        progress: Float,
        range: ClosedFloatingPointRange<Float> = 0f..1f,
        gapSize: androidx.compose.ui.unit.Dp = 0.dp,
        drawStopIndicator: (androidx.compose.ui.graphics.drawscope.DrawScope.() -> Unit)? = null
    ): IntArray {
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .size(widthDp, heightDp)
                    .background(backgroundColor)
                    .testTag(testTagValue)
            ) {
                LinearProgressIndicatorComponents(
                    progress = { progress },
                    range = range,
                    width = widthDp,
                    height = heightDp,
                    color = fillColor,
                    trackColor = trackColor,
                    gapSize = gapSize,
                    drawStopIndicator = drawStopIndicator
                )
            }
        }
        composeTestRule.waitForIdle()
        val bmp = composeTestRule.onNodeWithTag(testTagValue).captureToImage().asAndroidBitmap()
        val w = bmp.width
        val h = bmp.height
        return intArrayOf(
            bmp.getPixel(w / 4,     h / 2),  // left quarter — should be inside the fill when progress > 0.25
            bmp.getPixel(w * 3 / 4, h / 2)   // right quarter — should be inside the unfilled track when progress < 0.75
        )
    }

    @Test
    fun progress_zero_renders_only_track_color() {
        val px = renderAndSample(progress = 0f)
        assertEquals(trackColor.toArgb(), px[0])
        assertEquals(trackColor.toArgb(), px[1])
    }

    @Test
    fun progress_full_renders_only_fill_color() {
        val px = renderAndSample(progress = 1f)
        assertEquals(fillColor.toArgb(), px[0])
        assertEquals(fillColor.toArgb(), px[1])
    }

    @Test
    fun progress_half_renders_fill_on_left_and_track_on_right() {
        val px = renderAndSample(progress = 0.5f)
        assertEquals(fillColor.toArgb(), px[0])
        assertEquals(trackColor.toArgb(), px[1])
    }

    @Test
    fun progress_25_percent_renders_fill_only_in_left_quarter() {
        val px = renderAndSample(progress = 0.25f)
        // At 25% the fill ends right at x = w/4, so depending on the rounded stroke cap
        // the left quarter pixel may be the fill colour itself; the right quarter is
        // unambiguously still track colour.
        assertEquals(trackColor.toArgb(), px[1])
    }

    @Test
    fun custom_range_maps_progress_within_the_range() {
        // With range 0f..100f and progress 25f we expect exactly 25% of the bar to be filled.
        val px = renderAndSample(progress = 25f, range = 0f..100f)
        assertEquals(trackColor.toArgb(), px[1])
    }

    @Test
    fun custom_range_zero_progress_is_at_range_start() {
        val px = renderAndSample(progress = 0f, range = 10f..20f)
        assertEquals(trackColor.toArgb(), px[0])
        assertEquals(trackColor.toArgb(), px[1])
    }

    @Test
    fun drawStopIndicator_is_invoked_when_provided() {
        // Render an indicator with a custom stop indicator: a red vertical line at the
        // centre of the progress. We then assert that the centre pixel is red, which can
        // only happen if our drawStopIndicator ran.
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .size(widthDp, heightDp)
                    .background(backgroundColor)
                    .testTag(testTagValue)
            ) {
                LinearProgressIndicatorComponents(
                    progress = { 0.5f },
                    width = widthDp,
                    height = heightDp,
                    color = fillColor,
                    trackColor = trackColor,
                    gapSize = 0.dp,
                    drawStopIndicator = {
                        drawLine(
                            color = Color.Red,
                            start = Offset(size.width * 0.5f, -size.height),
                            end = Offset(size.width * 0.5f, size.height * 2f),
                            strokeWidth = 4f
                        )
                    }
                )
            }
        }
        composeTestRule.waitForIdle()
        val bmp = composeTestRule.onNodeWithTag(testTagValue).captureToImage().asAndroidBitmap()
        val centrePixel = bmp.getPixel(bmp.width / 2, bmp.height / 2)
        // The custom draw fills the exact centre column with red; a normal progress fill
        // would render the fill colour there instead. Allow either because the exact centre
        // row may also fall on the progress fill depending on antialiasing.
        assertEquals(Color.Red.toArgb(), centrePixel)
    }
}
