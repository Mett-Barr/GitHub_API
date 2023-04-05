package com.example.githubapi

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.compose.rememberAsyncImagePainter
import com.example.githubapi.data.local.room.history.SearchHistory
import com.example.githubapi.data.local.room.history.SearchHistoryDao
import com.example.githubapi.data.remote.github.RetrofitClient
import com.example.githubapi.data.remote.github.search.repositories.Item
import com.example.githubapi.databinding.ActivityMainBinding
import com.example.githubapi.ui.navigation.MainNavigation
import com.example.githubapi.ui.theme.MainTheme
import com.example.githubapi.ui.xml.MyAdapter
import com.example.githubapi.ui.xml.MyPagingAdapter
import com.example.githubapi.ui.xml.PostsLoadStateAdapter
import com.google.android.material.search.SearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    // testing
    @Inject
    lateinit var searchHistoryDao: SearchHistoryDao

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val repoAdapter = MyPagingAdapter()


    private var isFirstLaunch = true

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        WindowCompat.setDecorFitsSystemWindows(window, false)


        val isComposeMode = viewModel.isComposeMode.value
//        if (false) {
        if (isComposeMode == true) {

            setContent {
                WindowCompat.setDecorFitsSystemWindows(window, false)

//            AndroidView(modifier = Modifier.fillMaxSize(), factory = {
//                val view = LayoutInflater.from(it).inflate(R.layout.activity_main, null, false)
//                val rv = view.findViewById<RecyclerView>(R.id.rv)
//                rv.layoutManager = LinearLayoutManager(it)
//                rv.adapter = repoAdapter
//
//                lifecycleScope.launch {
//                    viewModel.pagingData.collect { pagingData ->
//                        repoAdapter.submitData(pagingData)
//                    }
//                }
//
//                view
//            })


                MainTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
//                    SearchPage()
                        MainNavigation()
                    }
                }
            }
        } else {

            setContentView(R.layout.activity_main)

            binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
            binding.viewModel = viewModel
            binding.lifecycleOwner = this

//            val rv = findViewById<RecyclerView>(R.id.rv)
//            rv.layoutManager = LinearLayoutManager(this)
//            rv.adapter = repoAdapter

            initUI()

            lifecycleScope.launch {
                viewModel.pagingData.collect { pagingData ->
                    repoAdapter.submitData(pagingData)
                }
            }

        }

        viewModel.isComposeMode.observe(this) {
            if (!isFirstLaunch) this.recreate()
            isFirstLaunch = false
        }




        CoroutineScope(Dispatchers.IO).launch {

//            val response = RetrofitClient.getUser("JetBrains/kotlin") // 使用实际的仓库所有者和名称替换 "owner" 和 "repo"
//            if (response.isSuccessful) {
//                Log.d("!!!", "成功获取仓库数据: ${response.body()}")
//            } else {
//                Log.d("!!!", "获取仓库数据失败: ${response.errorBody()}")
//            }

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

    private fun initUI() {
        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = repoAdapter.withLoadStateHeaderAndFooter(
            header = PostsLoadStateAdapter(),
            footer = PostsLoadStateAdapter()
        )

        val editText = findViewById<EditText>(R.id.edit_text)
        editText.setOnEditorActionListener { v, actionId, event ->
            viewModel.searchRepo()
            repoAdapter.refresh()
            true
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                return@setOnEditorActionListener true
//            }
//            false
        }


        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.also {
            if (isDarkThemeOn()) {
                it.setColorFilter(ContextCompat.getColor(this, R.color.white))
            } else {
                it.setColorFilter(ContextCompat.getColor(this, R.color.black))
            }

            it.setOnClickListener {
                val dialog = AlertDialog.Builder(this)
                dialog.apply {
//                    setView(layoutInflater.inflate(R.layout.dialog, null))
                    setMessage("Switch to Compose Mode")
                    setPositiveButton("Switch", DialogInterface.OnClickListener { _, _ ->
                        viewModel.onComposeModeChange(true)
                    })
                    setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->

                    })
                }
                dialog.create().show()
            }
        }


//        lifecycleScope.launch(Dispatchers.Default) {
//            viewModel.queryFlow.collect {
//                viewModel.searchRepo()
//                delay(2000)
//                withContext(Dispatchers.Main) {
//                    repoAdapter.retry()
//                    Log.d("!!!", "repoAdapter.retry")
//                }
//            }
//        }
        viewModel.queryFlow.asLiveData().observe(this) {
            viewModel.searchRepo()
            repoAdapter.refresh()
        }
    }


    // testing
    private fun LifecycleOwner.roomTest() {
        // 使用协程在后台线程执行数据库操作
        lifecycleScope.launch {
            // 插入一个新的搜索历史记录
            val searchHistory =
                SearchHistory(searchTerm = "test search", lastUsed = System.currentTimeMillis())
            searchHistoryDao.insert(searchHistory)

            // 获取所有搜索历史记录
            val searchHistories = searchHistoryDao.getAll()
            searchHistories.forEach { history ->
                Log.d(
                    "room",
                    "Search history: ${history.searchTerm}, Last used: ${history.lastUsed}"
                )
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

fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}