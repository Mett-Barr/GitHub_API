package com.example.githubapi.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.githubapi.MainViewModel

@Composable
fun RepoPaging(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()

    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(top = 72.dp)) {

        itemsIndexed(pagingItems) { index, it ->

        }
    }
}