package com.example.githubapi.ui.page

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.githubapi.MainViewModel
import com.example.githubapi.data.remote.github.getrepo.json.GetRepoItem
import com.example.githubapi.ui.component.RepoCard
import com.example.githubapi.ui.navigation.MainRoute

@Composable
fun BookmarkPage(viewModel: MainViewModel = hiltViewModel()) {
    val bookmarksList = viewModel.bookmarks.observeAsState()

    bookmarksList.value?.forEach {
        Log.d("!!!", "BookmarkPage: $it")
    }

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(bookmarksList.value ?: emptyList()) {

//            Spacer(modifier = Modifier
//                .clickable {
//                    viewModel.navigate(MainRoute.Settings)
//                    Log.d("!!!", "BookmarkPage: onClick ")
//                }
//                .background(Color.Green)
//                .size(150.dp))
//
            var repoItem by remember {
                mutableStateOf<GetRepoItem?>(null)
            }


            LaunchedEffect(Unit) {
                repoItem = viewModel.getRepo(it.fullName)
//                Log.d("!!!", "BookmarkPage LaunchedEffect $repoItem")
            }

            val isBookmarkExist = viewModel.isBookmarkExists(it.fullName).observeAsState()
            repoItem?.let { item ->

                Log.d("!!!", "BookmarkPage: $item")
                RepoCard(
                    item = item,
                    isBookmarkExist = isBookmarkExist.value ?: false,
                    onBookmarkChange = {
                        if (isBookmarkExist.value == true) {
                            viewModel.deleteBookmark(it.id)
                        } else {
                            viewModel.insertBookmark(it.fullName)
                        }
                    },
                    onClick = {
                        Log.d("!!!", "BookmarkPage onClick ${viewModel.route.value.route}")
//                        viewModel.navigate(MainRoute.Settings)
                        viewModel.navigate(MainRoute.Repository(item.html_url))
//                        viewModel.route.value = MainRoute.Repository(item.html_url)
                        Log.d("!!!", "BookmarkPage onClick ${viewModel.route.value.route}")
                    })
            }
        }
    }
}