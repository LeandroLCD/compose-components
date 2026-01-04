package com.blipblipcode.component.slider

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider as MaterialSlider
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderComponent(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    thumbSize:DpSize = DpSize(20.dp, 20.dp),
    trackHeight: Dp = 10.dp,
    tickSize:Dp = 6.dp,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColorsDefaults = SliderDefaults.colors(
        thumbColor = MaterialTheme.colorScheme.primary,
        activeTrackColor = MaterialTheme.colorScheme.surfaceContainer,
        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLow,
        activeTickColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        inactiveTickColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        disabledThumbColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
        disabledActiveTrackColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f),
        disabledInactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.7f),
        disabledActiveTickColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f),
        disabledInactiveTickColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.7f),
        ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: (@Composable (SliderState) -> Unit) = { sliderState ->
        SliderDefaults.Thumb(
            interactionSource = interactionSource,
            colors = colors,
            enabled = enabled,
            thumbSize = thumbSize
        )
    },
    track: (@Composable (SliderState) -> Unit) = { sliderState ->
        SliderDefaults.Track(
            colors = colors,
            enabled = enabled,
            sliderState = sliderState,
            trackHeight = trackHeight,
            tickSize = tickSize
        )
    },
    steps: Int = 0,
) {

    MaterialSlider(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        enabled = enabled,
        onValueChangeFinished = onValueChangeFinished,
        interactionSource = interactionSource,
        colors = colors.toMaterialSliderColors(),
        steps = steps,
        thumb = { sliderState ->
            thumb(sliderState)
        },
        track = { sliderState ->
            track(sliderState)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderComponent(
    state: SliderState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    thumbSize: DpSize = DpSize(20.dp, 20.dp),
    trackHeight: Dp = 10.dp,
    tickSize: Dp = 6.dp,
    colors: SliderColorsDefaults = SliderDefaults.colors(
        thumbColor = MaterialTheme.colorScheme.primary,
        activeTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        activeTickColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        inactiveTickColor = MaterialTheme.colorScheme.primary,
        disabledThumbColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
        disabledActiveTrackColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f),
        disabledInactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.7f),
        disabledActiveTickColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f),
        disabledInactiveTickColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.7f),
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: (@Composable (SliderState) -> Unit) = { sliderState ->
        SliderDefaults.Thumb(
            interactionSource = interactionSource,
            colors = colors,
            enabled = enabled,
            thumbSize = thumbSize
        )
    },
    track: (@Composable (SliderState) -> Unit) = { sliderState ->
        SliderDefaults.Track(
            colors = colors,
            enabled = enabled,
            sliderState = sliderState,
            trackHeight = trackHeight,
            tickSize = tickSize
        )
    },
) {
    MaterialSlider(
        state = state,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = colors.toMaterialSliderColors(),
        thumb = thumb,
        track = track
    )
}
