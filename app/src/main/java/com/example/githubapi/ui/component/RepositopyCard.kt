package com.example.githubapi.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFF
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoCard() {
    Card(
//        colors = CardColors(contentColor = Color.Gray),
        modifier = Modifier
            .padding(10.dp)
            .size(100.dp),
        onClick = { }
    ) {

    }
}