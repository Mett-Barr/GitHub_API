package com.example.githubapi.ui.component

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.example.githubapi.MainViewModel
import com.example.githubapi.data.remote.github.search.repositories.Item
import kotlinx.coroutines.delay

@Composable
fun SearchRepoColumn(
//    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    pagingItems: LazyPagingItems<Item>,
//    isBookmarkExist: Boolean = false,
//    onBookmarkChange: () -> Unit = {},
    navigate: (String) -> Unit
) {
//    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()

    val query = viewModel.queryFlow.collectAsState()
    val oauth = viewModel.oauth.observeAsState()
    val sort = viewModel.sort.observeAsState()
    val perPage = viewModel.perPage.observeAsState()

    LaunchedEffect(query.value, oauth.value, sort.value, perPage.value) {
        viewModel.searchRepo()
        delay(1500)
        pagingItems.refresh()

        Log.d("!!!", "SearchRepoColumn refresh ")
    }

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 72.dp, start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

//        item {
//            Box(modifier = Modifier.fillMaxWidth()) {
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .height(48.dp)
//                        .padding(8.dp)
//                )
//            }
//        }

        items(pagingItems) { it ->
            if (it != null) {

                val isBookmarkExist = viewModel.isBookmarkExists(it.full_name).observeAsState()

                RepoCard(
                    item = it,
                    isBookmarkExist = isBookmarkExist.value ?: false,
                    onBookmarkChange = {
                        if (isBookmarkExist.value == true) {
                            viewModel.deleteBookmarkByFullName(it.full_name)
                        } else {
                            viewModel.insertBookmark(it.full_name)
                        }
                    },
                    onClick = {
                        navigate(it.html_url)
                    })
            }
        }
    }
}