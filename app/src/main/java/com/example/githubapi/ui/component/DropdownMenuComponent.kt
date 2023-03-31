package com.example.githubapi.ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.githubapi.R

@Composable
fun DropdownDemo() {
    val items = listOf("A", "B", "C", "D", "E", "F")
    val disabledValue = "B"
    var expanded by remember { mutableStateOf(true) }

    Box(modifier = Modifier
//        .width(intrinsicSize = IntrinsicSize.Min)
        .background(Color.Yellow)
        .clickable { expanded = true }
//        .fillMaxSize()
        .wrapContentSize(Alignment.TopStart)) {
        Button(onClick = { expanded = !expanded }) {
            Text(expanded.toString())

        }
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
        }
        DropdownMenu(
            modifier = Modifier.clip(CircleShape),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                modifier = Modifier.clip(RoundedCornerShape(20.dp)),

                onClick = { /* Handle refresh! */ }, text = {
                    Text("Refresh")
                })
            DropdownMenuItem(onClick = { /* Handle settings! */ }, text = {
                Text("Settings")
            })
            Divider()
            DropdownMenuItem(onClick = { /* Handle send feedback! */ }, text = {
                Text("Send Feedback")
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <reified T : Enum<T>> SettingChips(
    chipText: String,
    crossinline select: (T) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box {
        AssistChip(
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            onClick = { expanded = true },
            trailingIcon = {
                Crossfade(targetState = expanded) { isExpanded ->

                    if (isExpanded) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.expand_less_fill0_wght400_grad0_opsz48),
                            contentDescription = "expand less"
                        )
                    } else
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.expand_more_fill0_wght400_grad0_opsz48),
                            contentDescription = "expand more"
                        )
                }
            },
            label = {
                Text(
                    text = chipText,
                    maxLines = 1
                )
            })
        DropdownSettingChips(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            select = select
        )
    }
}

@Composable
inline fun <reified T : Enum<T>> DropdownSettingChips(
    expanded: Boolean,
    noinline onDismissRequest: () -> Unit,
    crossinline select: (T) -> Unit
) {

    val enumValues = enumValues<T>().toList()

    DropdownMenu(
        modifier = Modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        enumValues.forEach {
            DropdownMenuItem(text = { Text(text = it.toString()) }, onClick = {
                select(it)
                onDismissRequest()
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <reified T : Enum<T>> TextFieldDropdownMenu(
    label: String,
    selectedOptionText: String,
    crossinline select: (T) -> Unit,
    forceClose: Boolean = true
) {
    val enumValues = enumValues<T>().toList()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded && forceClose,
            onDismissRequest = { expanded = false },
        ) {
            enumValues.forEach {
                DropdownMenuItem(
                    text = { Text(it.toString()) },
                    onClick = {
                        select(it)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}