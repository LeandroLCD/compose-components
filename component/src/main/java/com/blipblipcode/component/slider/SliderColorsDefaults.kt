package com.blipblipcode.component.slider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

@Immutable
data class SliderColorsDefaults(
    val thumbColor: Color,
    val activeTrackColor: Color,
    val activeTickColor: Color,
    val inactiveTrackColor: Color,
    val inactiveTickColor: Color,
    val disabledThumbColor: Color,
    val disabledActiveTrackColor: Color,
    val disabledActiveTickColor: Color,
    val disabledInactiveTrackColor: Color,
    val disabledInactiveTickColor: Color
) {
    @Composable
    fun thumbColor(enabled: Boolean): State<Color> =
        rememberUpdatedState(if (enabled) thumbColor else disabledThumbColor)

    @Composable
    fun trackColor(enabled: Boolean, active: Boolean): State<Color> = rememberUpdatedState(
        if (enabled) {
            if (active) activeTrackColor else inactiveTrackColor
        } else {
            if (active) disabledActiveTrackColor else disabledInactiveTrackColor
        }
    )

    @Composable
    fun tickColor(enabled: Boolean, active: Boolean): State<Color> = rememberUpdatedState(
        if (enabled) {
            if (active) activeTickColor else inactiveTickColor
        } else {
            if (active) disabledActiveTickColor else disabledInactiveTickColor
        }
    )
}