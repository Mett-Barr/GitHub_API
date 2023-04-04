package com.example.githubapi.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.githubapi.MainViewModel
import com.example.githubapi.ThemeMode

@Composable
fun SettingsPage(viewModel: MainViewModel = hiltViewModel()) {
    val isComposeMode by viewModel.isComposeMode.observeAsState(true)
    val mode by viewModel.themeMode.observeAsState()

//    LaunchedEffect(key1 = , block = )

    var showDialog by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .clickable { viewModel.onComposeModeChange(!isComposeMode) }
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Compose Mode", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.weight(1f))


            Switch(
                checked = isComposeMode ?: true,
//                onCheckedChange = { viewModel.onComposeModeChange(it) }
                onCheckedChange = viewModel::onComposeModeChange
            )
        }
        Column(modifier = Modifier
            .clickable {
                showDialog = true
            }
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(text = "Theme", style = MaterialTheme.typography.headlineSmall)
            Text(
                when (mode) {
                    ThemeMode.LIGHT -> "Light"
                    ThemeMode.DARK -> "Dark"
                    ThemeMode.SYSTEM_DEFAULT -> "System default"
                    else -> "System default"
                },
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier.padding(horizontal = 8.dp)
            )
//            Spacer(modifier = Modifier.weight(1f))

//            val isComposeMode by viewModel.isComposeMode.observeAsState(true)

//            Switch(checked = isComposeMode ?: true, onCheckedChange = viewModel::onComposeModeChange)
        }
    }

    if (showDialog) {


        Dialog(onDismissRequest = { showDialog = false }) {
            Card(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Theme", style = MaterialTheme.typography.headlineSmall)
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { viewModel.onThemeModeChange(ThemeMode.LIGHT) }) {
                        RadioButton(
                            selected = mode == ThemeMode.LIGHT,
                            onClick = { viewModel.onThemeModeChange(ThemeMode.LIGHT) })
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Light", style = MaterialTheme.typography.headlineSmall)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { viewModel.onThemeModeChange(ThemeMode.DARK) }) {
                        RadioButton(
                            selected = mode == ThemeMode.DARK,
                            onClick = { viewModel.onThemeModeChange(ThemeMode.DARK) })
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Dark", style = MaterialTheme.typography.headlineSmall)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { viewModel.onThemeModeChange(ThemeMode.SYSTEM_DEFAULT) }) {
                        RadioButton(
                            selected = mode == ThemeMode.SYSTEM_DEFAULT,
                            onClick = { viewModel.onThemeModeChange(ThemeMode.SYSTEM_DEFAULT) })
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "System default",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Row(Modifier.align(Alignment.End).clip(RoundedCornerShape(50)).clickable { showDialog = false }.padding(horizontal = 16.dp, vertical = 8.dp)) {
//                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "Cancel", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}