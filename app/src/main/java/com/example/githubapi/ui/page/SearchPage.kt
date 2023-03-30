package com.example.githubapi.ui.page

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.githubapi.MainViewModel
import com.example.githubapi.ui.component.SearchBar

@Composable
fun SearchPage(
    viewModel: MainViewModel = viewModel(),
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
//        TestPaging(modifier = Modifier)

        LazyColumn(
            contentPadding = PaddingValues(top = 72.dp)
        ) {
            items(100) {
                Spacer(modifier = Modifier
                    .padding(10.dp)
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .size(100.dp))
            }
        }

        /** Mask */
//        val isSearchBarExpanded by viewModel.isSearchBarExpanded
        val alpha: Float by animateFloatAsState(if (viewModel.isSearchBarExpanded) 0.5f else 0f)
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha))
                .then(
                    if (viewModel.isSearchBarExpanded) Modifier.clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        viewModel.onSearchBarStateChange(false)
                    } else {
                        Modifier
                    })
        )

        SearchBar(modifier = Modifier.align(Alignment.TopCenter))
    }
}