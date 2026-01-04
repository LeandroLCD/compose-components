package com.blipblipcode.component.range

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.blipblipcode.component.slider.SliderColorsDefaults

@OptIn(ExperimentalMaterial3Api::class)
object RangeSliderDefaults {
    @Composable
    fun Track(
        rangeSliderState: RangeSliderState,
        modifier: Modifier = Modifier,
        colors: SliderColorsDefaults,
        enabled: Boolean = false,
        trackHeight: Dp,
        tickSize: Dp,
        steps: Int
    ) {
        val inactiveTrackColor = if (enabled) colors.inactiveTrackColor else colors.disabledInactiveTrackColor
        val activeTrackColor = if (enabled) colors.activeTrackColor else colors.disabledActiveTrackColor
        val inactiveTickColor = if (enabled) colors.inactiveTickColor else colors.disabledInactiveTickColor
        val activeTickColor = if (enabled) colors.activeTickColor else colors.disabledActiveTickColor

        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(trackHeight)
        ) {
            val isRtl = layoutDirection == LayoutDirection.Rtl
            val sliderLeft = Offset(0f, center.y)
            val sliderRight = Offset(size.width, center.y)
            val sliderStart = if (isRtl) sliderRight else sliderLeft
            val sliderEnd = if (isRtl) sliderLeft else sliderRight
            val tickFractions = if (steps > 0) {
                (0..steps + 1).map { it.toFloat() / (steps + 1) }
            } else {
                emptyList()
            }

            val valueRange = rangeSliderState.valueRange
            val range = valueRange.endInclusive - valueRange.start
            val sliderValueEnd = rangeSliderState.activeRangeEnd
            val sliderValueStart = rangeSliderState.activeRangeStart

            val normalizedStart = if (range == 0f) 0f else (sliderValueStart - valueRange.start) / range
            val normalizedEnd = if (range == 0f) 0f else (sliderValueEnd - valueRange.start) / range

            val trackStrokeWidth = trackHeight.toPx()
            drawLine(
                inactiveTrackColor,
                sliderStart,
                sliderEnd,
                trackStrokeWidth,
                StrokeCap.Round
            )
            drawLine(
                activeTrackColor,
                Offset(size.width * normalizedStart, center.y),
                Offset(size.width * normalizedEnd, center.y),
                trackStrokeWidth,
                StrokeCap.Round
            )

            if (steps > 0) {
                tickFractions.forEach { fraction ->
                    val x = lerp(sliderStart, sliderEnd, fraction).x
                    val color = if (fraction in normalizedStart..normalizedEnd) {
                        activeTickColor
                    } else {
                        inactiveTickColor
                    }
                    drawCircle(
                        color = color,
                        center = Offset(x, center.y),
                        radius = tickSize.toPx() / 2
                    )
                }
            }
        }
    }
}
