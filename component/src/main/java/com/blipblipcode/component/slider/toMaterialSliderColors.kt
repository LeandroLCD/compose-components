package com.blipblipcode.component.slider

import androidx.compose.material3.SliderColors
import androidx.compose.runtime.Composable
import androidx.compose.material3.SliderDefaults as MaterialSliderDefaults

@Composable
    fun SliderColorsDefaults.toMaterialSliderColors(): SliderColors {
        return MaterialSliderDefaults.colors(
            thumbColor = thumbColor(enabled = true).value,
            disabledThumbColor = thumbColor(enabled = false).value,
            activeTrackColor = trackColor(enabled = true, active = true).value,
            inactiveTrackColor = trackColor(enabled = true, active = false).value,
            disabledActiveTrackColor = trackColor(enabled = false, active = true).value,
            disabledInactiveTrackColor = trackColor(enabled = false, active = false).value
        )
    }