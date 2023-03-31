package com.example.githubapi

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.example.githubapi.data.local.room.SearchHistory
import com.example.githubapi.data.local.room.SearchHistoryDao
import com.example.githubapi.data.remote.github.search.repositories.Item
import com.example.githubapi.ui.component.SearchBar
import com.example.githubapi.ui.page.SearchPage
import com.example.githubapi.ui.page.TestPaging
import com.example.githubapi.ui.theme.MainTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    // testing
    @Inject
    lateinit var searchHistoryDao: SearchHistoryDao

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

//        setContentView(R.layout.activity_main)



//        roomTest()
//        testRoomLiveData()


        setContent {

            MainTheme {
//                Spacer(modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Gray))
//
//            }

////                Spacer(modifier = Modifier
////                    .fillMaxSize()
////                    .background(Color.Gray))
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchPage()
                }
            }
//            if (viewModel.isComposeMode) {
//                TestUI { viewModel.test() }
//            } else {
//                AndroidView(
//                    factory = {
//                        LayoutInflater.from(it).inflate(R.layout.activity_main, null)
//                    },
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clickable { viewModel.test() }
//                        .background(Color.White)
//                )
//            }

//            TestColumn(repo = viewModel.gitRepo)

//            Column {
//                Switch(
//                    checked = viewModel.isUsingOauth,
//                    onCheckedChange = { viewModel.isUsingOauth = !viewModel.isUsingOauth })
////                Button(onClick = { viewModel.invaTest() }) {
////                    Text(text = "test")
////                }
////                TextField(value = viewModel.searchTextFlow.collectAsState().value, onValueChange =
////                { textFieldValue ->
//////                    viewModel.textFieldValue = textFieldValue
//////                    viewModel.searchRepo()
////                })
////                TestPaging()
//
//                SearchBar()
//            }

        }

        CoroutineScope(Dispatchers.IO).launch {
//            RetrofitClient.test()

//            val retrofit = Retrofit.Builder()
//                .baseUrl("https://api.github.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//
//            val githubApi = retrofit.create(GitHubApiService::class.java)
//
//            val call = githubApi.searchRepositories(
//                "token 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",
//                "kotlin"
//            )
//
//            val response = call.execute()
//            if (response.isSuccessful) {
//                val responseBody = response.body()
////                        responseBody?.items?.forEach { repo ->
////                            println("${repo.name} (${repo.fullName}): ${repo.htmlUrl}")
////                        }
//                responseBody?.items?.forEach { repo ->
//                    Log.d("api", repo.toString())
//                }
//            } else {
//                println("API 请求失败: ${response.code()} ${response.message()}")
//            }
//
        }
    }


    // testing
    private fun LifecycleOwner.roomTest() {
        // 使用协程在后台线程执行数据库操作
        lifecycleScope.launch {
            // 插入一个新的搜索历史记录
            val searchHistory = SearchHistory(searchTerm = "test search", lastUsed = System.currentTimeMillis())
            searchHistoryDao.insert(searchHistory)

            // 获取所有搜索历史记录
            val searchHistories = searchHistoryDao.getAll()
            searchHistories.forEach { history ->
                Log.d("room","Search history: ${history.searchTerm}, Last used: ${history.lastUsed}")
            }
        }
    }
    private fun testRoomLiveData() {
        lifecycleScope.launch {
            // 插入示例数据
            viewModel.insertSearchHistory("Test Search 1")
            Log.d("RoomLiveDataTest", "Inserted Test Search 1")
            printSearchHistories()

            // 等待一会儿，然后插入另一个搜索历史记录
            delay(3000)
            viewModel.insertSearchHistory("Test Search 2")
            Log.d("RoomLiveDataTest", "Inserted Test Search 2")
            printSearchHistories()

            // 更新示例数据
            delay(3000)
            viewModel.searchHistories.value?.firstOrNull()?.let { firstSearchHistory ->
                viewModel.updateSearchHistory(firstSearchHistory.copy(searchTerm = "Updated Search Term"))
                Log.d("RoomLiveDataTest", "Updated first search history")
            }
            printSearchHistories()

            // 删除示例数据
            delay(3000)
            viewModel.searchHistories.value?.firstOrNull()?.let { firstSearchHistory ->
                viewModel.deleteSearchHistory(firstSearchHistory.id)
                Log.d("RoomLiveDataTest", "Deleted first search history")
            }
            printSearchHistories()

            // 删除所有搜索历史记录
            delay(3000)
            viewModel.deleteAllSearchHistories()
            Log.d("RoomLiveDataTest", "Deleted all search histories")
            printSearchHistories()
        }
    }
    private suspend fun printSearchHistories() {
        val searchHistories = viewModel.getAllSearchHistories()
        Log.d("RoomLiveDataTest", "Search histories changed:")
        searchHistories.forEach {
            Log.d("RoomLiveDataTest", "${it.id}: ${it.searchTerm}, ${it.lastUsed}")
        }
    }

}

@Composable
fun TestUI(click: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { click() }
            .background(Color.White)
    ) {
        Text("Compose")
    }
}

@Composable
fun TestColumn(repo: List<Item>) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        items(repo) {
            Card(Modifier.padding(20.dp)) {
                Row {
                    Image(
                        painter = rememberAsyncImagePainter(it.owner.avatar_url),
                        contentDescription = it.owner.url,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(
                                RoundedCornerShape(
                                    if (it.owner.type == "Organization") 15
                                    else 100
                                )
                            )
                    )
                    Column {
                        Text(text = it.name)
//                        Text(text = it.description)
                        Text(text = it.language ?: "null")
//                        Text(text = it.stargazers_count.toString())
                    }
                }
            }
        }
    }
}

//@Composable
//fun TestColumn2(repo: GitHubRepositories) {
//    LazyColumn(
//        Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .padding(20.dp)) {
//        items(repo.items) {
//            Card(Modifier.padding(20.dp)) {
//                Row {
//                    Image(
//                        painter = rememberAsyncImagePainter(it.archive_url),
//                        contentDescription = it.name
//                    )
//                    Column {
//                        Text(text = it.name)
//                        Text(text = it.archive_url)
//                    }
//                }
//            }
//        }
//    }
//}