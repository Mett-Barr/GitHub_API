package com.example.githubapi.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun RippleOnOtherClickDemo() {
    var clickCount by remember { mutableStateOf(0) }
    var clickPosition by remember { mutableStateOf(Offset.Zero) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Blue)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = { offset ->
                        clickPosition = offset
                        clickCount++
                    })
                }
        )

        Box(
            modifier = Modifier
                .size(100.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        // Handle click
                    }
                )
                .background(Color.Red)
                .offset {
                    IntOffset(clickPosition.x.toInt(), clickPosition.y.toInt()) - IntOffset(50, 50)
                }
                .indication(
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        color = Color.Gray.copy(alpha = 0.5f),
                        bounded = false,
//                        radius = 300.dp
                    )
                )
        ) {
            Text(
                text = "Clicks: $clickCount",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
fun TestAnim() {
    var boo by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().clickable{ boo = !boo}) {


        AnimatedVisibility(visible = boo) {
            Spacer(modifier = Modifier
                .background(Color.Gray)
                .height(50.dp)
                .fillMaxWidth())
        }
        Spacer(modifier = Modifier
            .background(Color.White)
            .weight(1f)
            .fillMaxWidth())
        AnimatedVisibility(visible = boo) {
            Spacer(
                modifier = Modifier
                    .background(Color.Gray)
                    .height(50.dp)
                    .fillMaxWidth()
            )
        }
    }
}
