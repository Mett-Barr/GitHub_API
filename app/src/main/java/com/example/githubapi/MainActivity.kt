package com.example.githubapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import dagger.hilt.android.AndroidEntryPoint

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