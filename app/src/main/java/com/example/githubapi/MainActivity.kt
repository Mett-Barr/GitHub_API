package com.example.githubapi

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.example.githubapi.data.remote.githubapi.RetrofitClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_main)

        setContent {
            if (viewModel.isComposeMode) {
                TestUI { viewModel.test() }
            } else {
                AndroidView(
                    factory = {
                        LayoutInflater.from(it).inflate(R.layout.activity_main, null)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { viewModel.test() }
                        .background(Color.White)
                )
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.test()

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