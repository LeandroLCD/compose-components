package com.blipblipcode.component.slider

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

object SliderSizeDefaults {

    val ThumbWidth = 18.dp
    val ThumbHeight = 18.dp
    val ThumbSize = DpSize(ThumbWidth, ThumbHeight)
    val ThumbDefaultElevation = 1.dp
    val ThumbPressedElevation = 6.dp

    val TrackHeight = 3.dp

    val TickRadius = RoundedCornerShape(50.dp)
    val TickRadiusRippleEffect = 15.dp
    val TickSize = 7.dp

}