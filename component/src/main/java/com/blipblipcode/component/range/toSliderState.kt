package com.blipblipcode.component.range

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SliderState

@OptIn(ExperimentalMaterial3Api::class)
fun RangeSliderState.toSliderState(steps: Int): SliderState {
    return SliderState(
        value = activeRangeStart,
        steps = steps,
        onValueChangeFinished = {},
        valueRange = valueRange
    )
}

