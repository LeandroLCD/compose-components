package com.blipblipcode.component.slider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection

@Stable
object SliderDefaults {
    @Composable
    fun colors(
        thumbColor: Color = Color.Unspecified,
        activeTrackColor: Color = Color.Unspecified,
        activeTickColor: Color = Color.Unspecified,
        inactiveTrackColor: Color = Color.Unspecified,
        inactiveTickColor: Color = Color.Unspecified,
        disabledThumbColor: Color = Color.Unspecified,
        disabledActiveTrackColor: Color = Color.Unspecified,
        disabledActiveTickColor: Color = Color.Unspecified,
        disabledInactiveTrackColor: Color = Color.Unspecified,
        disabledInactiveTickColor: Color = Color.Unspecified
    ): SliderColorsDefaults = SliderColorsDefaults(
        thumbColor = thumbColor,
        activeTrackColor = activeTrackColor,
        activeTickColor = activeTickColor,
        inactiveTrackColor = inactiveTrackColor,
        inactiveTickColor = inactiveTickColor,
        disabledThumbColor = disabledThumbColor,
        disabledActiveTrackColor = disabledActiveTrackColor,
        disabledActiveTickColor = disabledActiveTickColor,
        disabledInactiveTrackColor = disabledInactiveTrackColor,
        disabledInactiveTickColor = disabledInactiveTickColor
    )

    @Composable
    fun Thumb(
        modifier: Modifier = Modifier,
        interactionSource: MutableInteractionSource,
        enabled: Boolean = true,
        colors: SliderColorsDefaults,
        thumbSize: DpSize = SliderSizeDefaults.ThumbSize
    ) {
        val interactions = remember { mutableStateListOf<Interaction>() }
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> interactions.add(interaction)
                    is PressInteraction.Release -> interactions.remove(interaction.press)
                    is PressInteraction.Cancel -> interactions.remove(interaction.press)
                    is DragInteraction.Start -> interactions.add(interaction)
                    is DragInteraction.Stop -> interactions.remove(interaction.start)
                    is DragInteraction.Cancel -> interactions.remove(interaction.start)
                }
            }
        }

        Spacer(
            modifier
                .size(thumbSize)
                .graphicsLayer {
                   // translationY = thumbSize.height.div(3).toPx()
                   // translationX = thumbSize.width.div(3).toPx()
                }
                .indication(
                    interactionSource = interactionSource,
                    indication = ripple(bounded = false, radius = SliderSizeDefaults.TickRadiusRippleEffect)
                )
                .hoverable(interactionSource = interactionSource)
                .shadow(
                    elevation = if (enabled && interactions.isNotEmpty()) {
                        SliderSizeDefaults.ThumbPressedElevation
                    } else {
                        SliderSizeDefaults.ThumbDefaultElevation
                    },
                    shape = SliderSizeDefaults.TickRadius,
                    clip = false
                )
                .background(colors.thumbColor(enabled).value, SliderSizeDefaults.TickRadius)
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Track(
        modifier: Modifier = Modifier,
        sliderState: SliderState,
        colors: SliderColorsDefaults,
        trackHeight: Dp = SliderSizeDefaults.TrackHeight,
        tickSize: Dp = SliderSizeDefaults.TickSize,
        enabled: Boolean = true
    ) {
        val inactiveTrackColor = colors.trackColor(enabled, active = false)
        val activeTrackColor = colors.trackColor(enabled, active = true)
        val inactiveTickColor = colors.tickColor(enabled, active = false)
        val activeTickColor = colors.tickColor(enabled, active = true)


        Canvas(
            modifier
                .fillMaxWidth()
                .height(trackHeight)
        ) {
            val isRtl = layoutDirection == LayoutDirection.Rtl
            val sliderLeft = Offset(0f, center.y)
            val sliderRight = Offset(size.width, center.y)
            val sliderStart = if (isRtl) sliderRight else sliderLeft
            val sliderEnd = if (isRtl) sliderLeft else sliderRight
            val tickFractions = if (sliderState.steps > 0) {
                (0..sliderState.steps + 1).map { it.toFloat() / (sliderState.steps + 1) }
            } else {
                emptyList()
            }

            val valueRange = sliderState.valueRange
            val range = valueRange.endInclusive - valueRange.start
            val normalizedValue = if (range == 0f) 0f else (sliderState.value - valueRange.start) / range

            val trackStrokeWidth = trackHeight.toPx()
            drawLine(
                inactiveTrackColor.value,
                sliderStart,
                sliderEnd,
                trackStrokeWidth,
                StrokeCap.Round
            )
            drawLine(
                activeTrackColor.value,
                sliderStart,
                Offset(lerp(sliderStart, sliderEnd, normalizedValue).x, center.y),
                trackStrokeWidth,
                StrokeCap.Round
            )

            if (sliderState.steps > 0) {
                tickFractions.forEach { fraction ->
                    val x = lerp(sliderStart, sliderEnd, fraction).x
                    val color = if (fraction <= normalizedValue) {
                        activeTickColor.value
                    } else {
                        inactiveTickColor.value
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


