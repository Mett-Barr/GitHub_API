package com.example.githubapi.ui.page

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.githubapi.MainViewModel
import com.example.githubapi.ui.component.SearchRepoColumn
import com.example.githubapi.ui.component.SearchBar
import com.example.githubapi.ui.navigation.MainRoute

@Composable
fun SearchPage(
    viewModel: MainViewModel = hiltViewModel(),
) {

//    viewModel.lastSearch
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .statusBarsPadding()
    ) {


        LaunchedEffect(viewModel.isSearchBarExpanded) {
            viewModel.apply {
                if (!isSearchBarExpanded) {
//                    Log.d("!!!", "queryFlow = ${queryFlow.value}, lastSearch = $lastSearch ")
                    if (queryFlow.value.isBlank()) {
                        queryFlow.value = lastSearch
                        searchRepo()
                    }
                }
            }
        }


        val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()
        SearchRepoColumn(pagingItems = pagingItems) {
            viewModel.navigate(MainRoute.Repository(it))
        }
//        RepoColumn(viewModel = viewModel)

        /** Mask */
//        val isSearchBarExpanded by viewModel.isSearchBarExpanded
        val alpha: Float by animateFloatAsState(if (viewModel.isSearchBarExpanded) 0.7f else 0f)
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha))
                .then(
                    if (viewModel.isSearchBarExpanded) {
                        Modifier.clickable(indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            viewModel.apply {
                                onSearchBarStateChange(false)
                                if (lastSearch != queryFlow.value)
//                                    searchRepo()
                                    searchRepoAndStore()
                            }
                        }
                    } else {
                        Modifier
                    })
        )

        SearchBar(modifier = Modifier.align(Alignment.TopCenter), viewModel)
    }
}