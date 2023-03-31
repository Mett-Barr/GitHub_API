package com.example.githubapi.ui.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.githubapi.data.local.room.SearchHistory
import com.example.githubapi.ui.theme.MainTheme
import kotlin.random.Random

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
    Column(
        Modifier
            .fillMaxSize()
            .clickable { boo = !boo }) {


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


fun generateRandomSearchHistories(): List<SearchHistory> {
    val searchHistories = mutableListOf<SearchHistory>()
    val random = Random(System.currentTimeMillis())

    for (i in 1..40) {
        val searchTerm = "Search Term ${random.nextInt(1, 101)}"
        val lastUsed = System.currentTimeMillis() - random.nextLong(1_000, 1_000_000)

        val searchHistory = SearchHistory(id = i, searchTerm = searchTerm, lastUsed = lastUsed)
        searchHistories.add(searchHistory)
    }

    return searchHistories
}

@Preview
@Composable
fun SearchHistoryTest() {
    Column(Modifier.padding(8.dp)) {
        SearchHistoriesCard(searchHistories = generateRandomSearchHistories())
    }
}

@Preview
@Composable
fun ColorTest() {
    MainTheme {
        Column {
            Spacer(modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colorScheme.primary))
            Spacer(modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colorScheme.secondary))
            Spacer(modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colorScheme.tertiary))
            Spacer(modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colorScheme.onSurface))
        }
    }
}


@Preview
@Composable
fun TextTest() {
    var text by remember { mutableStateOf("123") }

    Column {
        TestTextField(value = text, onValueChange = { newText ->
            text = newText
        })

        Button(onClick = {
            // 在此處更新 text 以模擬外部更改
            text = "New external value"
        }) {
            Text("Update text")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestTextField(value: String, onValueChange: (String) -> Unit) {
    var selection by remember { mutableStateOf(TextRange(value.length)) }
    val textFieldValue by remember(value, selection) {
        derivedStateOf {
            TextFieldValue(text = value, selection = selection)
        }
    }

    TextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            Log.d("!!!", "TextField: onValueChange value = $value")
            onValueChange(newValue.text)
            selection = newValue.selection
        }
    )
}