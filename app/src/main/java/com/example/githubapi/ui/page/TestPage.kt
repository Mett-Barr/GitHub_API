package com.example.githubapi.ui.page

//import com.example.githubapi.RateLimitException
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.githubapi.MainViewModel
import com.example.githubapi.R
import com.example.githubapi.data.remote.github.search.repositories.Item
import com.example.githubapi.ui.component.RepoCard
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.debounce

//import com.example.githubapi.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestPaging(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
//    paddingValues: PaddingValues
) {

    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()

    var needResearching by remember {
        mutableStateOf(false)
    }


    val listState = rememberLazyListState()

    val searchText by viewModel.queryFlow.collectAsState()

//    viewModel.searchRepo()

//    Log.d("!!!", pagingItems.itemCount.toString())

    LaunchedEffect(key1 = pagingItems.itemCount) {
        Log.d("!!!", "itemCount = " + pagingItems.itemCount.toString())
    }

//    LaunchedEffect(viewModel.lastSearch) {
//        pagingItems.refresh()
//    }

    LaunchedEffect(searchText) {
        viewModel.queryFlow
            .debounce(2000L) // 2 seconds debounce
            .collect { searchText ->
                Log.d("!!!", "lastSearch = ${viewModel.lastSearch}")
                if (searchText.isNotBlank()) {
                    viewModel.searchRepo()
                    pagingItems.refresh()
                }
            }
    }

    LaunchedEffect(viewModel.lastSearch) {
        Log.d("!!!", "lastSearch = ${viewModel.lastSearch}")
        pagingItems.refresh()
    }


//    LaunchedEffect(searchText) {
//        viewModel.searchRepo()
//        pagingItems.refresh()
//
//        val thisTime = needResearching
//        needResearching = false
//
//        Log.d("!!!", "$thisTime ")
//        Log.d("!!!", "$needResearching ")
//
//        withContext(Dispatchers.Default) {
//            delay(2000)
//            if (needResearching) {
//                test()
//            }
//        }
//    }


    Column {

//        Button(onClick = { pagingItems.refresh() }) {
//            Text(text = "re")
//        }
//        TextField(
//            value = searchText,
//            onValueChange = { textFieldValue -> viewModel.queryFlow.value = textFieldValue })

//        SearchBar()

        LazyColumn(
//            Modifier
            modifier
                .fillMaxSize()
//                .padding(paddingValues)
                .clickable { pagingItems.refresh() },
//                .background(Color.White)
//                .padding(20.dp)

            contentPadding = PaddingValues(top = 72.dp)
        ) {


            // test
//            items(50) {
//                CardSample()
//            }

            itemsIndexed(pagingItems) { index, it ->
                if (pagingItems.loadState.refresh is LoadState.Loading) {
                    Spacer(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.Gray)
                    )
                } else {
                    if (it == null) {
                        // 显示占位符

                        Spacer(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(
                                    Color.Yellow
                                )
                        )

                    } else {
                        // 显示repository项

                        Log.d("!!!", it.toString())

//                        Card(Modifier.padding(20.dp)) {
//                            Row {
//                                Text(text = (index + 1).toString())
//                                Image(
//                                    painter = rememberAsyncImagePainter(it.owner.avatar_url),
//                                    contentDescription = it.owner.url,
//                                    modifier = Modifier
//                                        .size(50.dp)
//                                        .clip(
//                                            RoundedCornerShape(
//                                                if (it.owner.type == "Organization") 15
//                                                else 100
//                                            )
//                                        )
//                                )
//                                Column {
//                                    Text(text = it.name)
////                        Text(text = it.description)
//                                    Text(text = it.language ?: "null")
////                        Text(text = it.stargazers_count.toString())
//                                }
//                            }
//                        }


//                        RepoCard(it)
                    }
                }

            }

            // 在列表底部显示加载状态
            displayLoadState(pagingItems = pagingItems)
//        pagingItems.apply {
//            this
//            when {
//                loadState.refresh is LoadState.Loading -> {
//                    item { Text(text = "正在请求...") }
//                }
//                loadState.append is LoadState.Loading -> {
//                    item { CircularProgressIndicator(modifier = Modifier.padding(8.dp)) }
//                }
//                loadState.refresh is LoadState.Error -> {
//                    val errorState = pagingItems.loadState.refresh as LoadState.Error
//                    item {
//                        Text(
//                            text = "请求失败: ${errorState.error.localizedMessage}",
//                            modifier = Modifier.padding(8.dp)
//                        )
//                    }
//                }
//                loadState.append is LoadState.Error -> {
//                    val errorState = pagingItems.loadState.append as LoadState.Error
//                    item {
//                        Text(
//                            text = "加载更多失败: ${errorState.error.localizedMessage}",
//                            modifier = Modifier.padding(8.dp)
//                        )
//                    }
//                }
//            }
//        }


//        pagingItems.apply {
//
//            if (viewModel.isRateLimited) {
//                item {
//                    Text(
//                        text = "已达到速率限制，请稍后重试。",
//                        modifier = Modifier.padding(8.dp),
//                        color = Color.Red
//                    )
//                }
//            } else {
//                when {
//                    loadState.refresh is LoadState.Loading -> {
//                        item { Text(text = "正在请求...") }
//                    }
//                    loadState.append is LoadState.Loading -> {
//                        item { CircularProgressIndicator(modifier = Modifier.padding(8.dp)) }
//                    }
//                    loadState.refresh is LoadState.Error -> {
//                        val errorState = pagingItems.loadState.refresh as LoadState.Error
//                        item {
//                            Column(modifier = Modifier.padding(8.dp)) {
//                                Text(
//                                    text = "请求失败: ${errorState.error.localizedMessage}",
//                                    modifier = Modifier.padding(bottom = 8.dp)
//                                )
//                                Button(onClick = { pagingItems.retry() }) {
//                                    Text("重新加载")
//                                }
//                            }
//                        }
//                    }
//                    loadState.append is LoadState.Error -> {
//                        val errorState = pagingItems.loadState.append as LoadState.Error
//                        item {
//                            Column(modifier = Modifier.padding(8.dp)) {
//                                Text(
//                                    text = "加载更多失败: ${errorState.error.localizedMessage}",
//                                    modifier = Modifier.padding(bottom = 8.dp)
//                                )
//                                Button(onClick = { pagingItems.retry() }) {
//                                    Text("重新加载")
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }

        }
    }
}

//@Composable
fun LazyListScope.displayLoadState(pagingItems: LazyPagingItems<Item>) {


    when {
        pagingItems.loadState.refresh is LoadState.Loading -> {
            item { Text(text = "正在请求...") }
        }
        pagingItems.loadState.append is LoadState.Loading -> {
            item { CircularProgressIndicator(modifier = Modifier.fillMaxWidth().padding(8.dp)) }
        }
        pagingItems.loadState.refresh is LoadState.Error -> {
            val errorState = pagingItems.loadState.refresh as LoadState.Error
            item {
                Text(
                    text = "请求失败: ${errorState.error.localizedMessage}",
                    modifier = Modifier.padding(8.dp)
                )

//                Log.d("!!!", "refresh is LoadState.Error: ${errorState.error}")
//                Log.d(
//                    "!!!",
//                    "refresh is LoadState.Error: ${errorState.error is RateLimitException}"
//                )
            }
        }
        pagingItems.loadState.append is LoadState.Error -> {
            val errorState = pagingItems.loadState.append as LoadState.Error
            item {
                Text(
                    text = "加载更多失败: ${errorState.error.localizedMessage}",
                    modifier = Modifier.padding(8.dp)
                )

//                Log.d("!!!", "append is LoadState.Error: ${errorState.error}")
//                Log.d("!!!", "append is LoadState.Error: ${errorState.error is RateLimitException}")

            }
        }
    }


}


@Preview
@Composable
fun TestBlur() {

    var boo by remember {
        mutableStateOf(false)
    }

    val blur = animateDpAsState(targetValue = if (boo) 0.dp else 10.dp)

    Icon(
        painter = painterResource(id = R.drawable.search_fill0_wght400_grad0_opsz48),
        contentDescription = "",
        modifier = Modifier
            .size(100.dp)
            .blur(blur.value)
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            boo = !boo
        }
    }
}

