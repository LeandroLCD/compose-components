package com.blipblipcode.component.range

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider as MaterialRangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.blipblipcode.component.slider.SliderColorsDefaults
import com.blipblipcode.component.slider.SliderDefaults
import com.blipblipcode.component.slider.toMaterialSliderColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSliderComponent(
    modifier: Modifier = Modifier,
    state: RangeSliderState,
    steps: Int = state.steps,
    enabled: Boolean = true,
    thumbSize: DpSize = DpSize(20.dp, 20.dp),
    trackHeight: Dp = 10.dp,
    tickSize: Dp = 6.dp,
    colors: SliderColorsDefaults = SliderDefaults.colors(
        thumbColor = MaterialTheme.colorScheme.primary,
        activeTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLow,
        activeTickColor = MaterialTheme.colorScheme.surfaceContainerLow,
        inactiveTickColor = MaterialTheme.colorScheme.primary,
        disabledThumbColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
        disabledActiveTrackColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f),
        disabledInactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.7f),
        disabledActiveTickColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f),
        disabledInactiveTickColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.7f),
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    startThumb: @Composable (RangeSliderState) -> Unit = {
        SliderDefaults.Thumb(
            interactionSource = interactionSource,
            colors = colors,
            enabled = enabled,
            thumbSize = thumbSize
        )
    },
    endThumb: @Composable (RangeSliderState) -> Unit = {
        SliderDefaults.Thumb(
            interactionSource = interactionSource,
            colors = colors,
            enabled = enabled,
            thumbSize = thumbSize
        )
    },
    track: @Composable (RangeSliderState) -> Unit = { rangeState ->
        RangeSliderDefaults.Track(
            colors = colors,
            enabled = enabled,
            rangeSliderState = rangeState,
            trackHeight = trackHeight,
            tickSize = tickSize,
            steps = steps
        )
    },
) {
    MaterialRangeSlider(
        state = state,
        modifier = modifier,
        enabled = enabled,
        colors = colors.toMaterialSliderColors(),
        startThumb = startThumb,
        endThumb = endThumb,
        track = track
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSliderComponent(
    value: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit = {},
    onValueChangeFinished: (() -> Unit)? = null,
    steps: Int ,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
    thumbSize: DpSize = DpSize(20.dp, 20.dp),
    trackHeight: Dp = 10.dp,
    tickSize: Dp = 6.dp,
    colors: SliderColorsDefaults = SliderDefaults.colors(
        thumbColor = MaterialTheme.colorScheme.primary,
        activeTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLow,
        activeTickColor = MaterialTheme.colorScheme.surfaceContainerLow,
        inactiveTickColor = MaterialTheme.colorScheme.primary,
        disabledThumbColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
        disabledActiveTrackColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f),
        disabledInactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.7f),
        disabledActiveTickColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f),
        disabledInactiveTickColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.7f),
    ),
    startInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    endInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    startThumb: @Composable (RangeSliderState) -> Unit = {
        SliderDefaults.Thumb(
            interactionSource = startInteractionSource,
            colors = colors,
            enabled = enabled,
            thumbSize = thumbSize
        )
    },
    endThumb: @Composable (RangeSliderState) -> Unit = {
        SliderDefaults.Thumb(
            interactionSource = endInteractionSource,
            colors = colors,
            enabled = enabled,
            thumbSize = thumbSize
        )
    },
    track: @Composable (RangeSliderState) -> Unit = { rangeState ->
        RangeSliderDefaults.Track(
            colors = colors,
            enabled = enabled,
            rangeSliderState = rangeState,
            trackHeight = trackHeight,
            tickSize = tickSize,
            steps = steps
        )
    },
) {
    MaterialRangeSlider(
        value = value,
        modifier = modifier,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        steps = steps,
        valueRange = valueRange,
        enabled = enabled,
        colors = colors.toMaterialSliderColors(),
        startThumb = startThumb,
        endThumb = endThumb,
        track = track
    )
}