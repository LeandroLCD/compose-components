package com.blipblipcode.compose_components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.blipblipcode.component.range.RangeSliderComponent
import com.blipblipcode.component.slider.LinearProgressIndicatorComponents
import com.blipblipcode.component.slider.SliderComponent
import com.blipblipcode.compose_components.ui.theme.ComposecomponentsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposecomponentsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestComponents(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestComponents(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().background(Color.Black).padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val sliderState = remember {
            SliderState(
                value = 3.333335f,
                steps = 5,
                valueRange = 0f..20f
            )
        }
        SliderComponent(state = sliderState)
        Text(text = "${sliderState.value}", color = Color.White)

        val rangeSliderState = remember {
            RangeSliderState(
                activeRangeStart = 0f,
                activeRangeEnd = 20f,
                steps = 19,
                valueRange = 0f..20f
            )
        }
        RangeSliderComponent(state = rangeSliderState)
        Text(text = "${rangeSliderState.activeRangeStart}-${rangeSliderState.activeRangeEnd}", color = Color.White)

        Spacer(Modifier.height(8.dp))

        LinearProgressIndicatorComponents(
            progress = { sliderState.value },
            range = sliderState.valueRange,
            color = Color.Cyan,
            trackColor = Color.DarkGray,
            height = 12.dp,
            drawStopIndicator = {
                val indicatorHeight = size.height * 1.2f
                val indicatorWidth = indicatorHeight
                val centerX = size.width * (sliderState.coercedValueAsFraction )
                val top = center.y - indicatorHeight / 2f
                val bottom = center.y + indicatorHeight / 2f
                drawLine(
                    color = Color.Red,
                    strokeWidth = indicatorWidth,
                    cap = StrokeCap.Round,
                    start = Offset(centerX, top),
                    end = Offset(centerX, bottom)
                )
            }
        )

        Spacer(Modifier.height(20.dp))

        LinearProgressIndicatorComponents(
            progress = { sliderState.value },
            range = sliderState.valueRange,
            color = Color.Blue,
            trackColor = Color.DarkGray,
            height = 12.dp
        )
    }
}


