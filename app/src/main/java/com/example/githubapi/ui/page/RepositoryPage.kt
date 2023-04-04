package com.example.githubapi.ui.page

import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RepositoryPage(url: String, navigationBack: () -> Unit = {}) {

    val coroutineScope = rememberCoroutineScope()

    var currentUrl by remember {
        mutableStateOf("")
    }

//    var isNewsPage by remember {
//        mutableStateOf(false)
//    }


    fun pageCheck(url: String) {

        Log.d("!!", "pageCheck: ")

        currentUrl = url

        coroutineScope.launch(Dispatchers.IO) {
//            Log.d("!!", "pageCheck: $isNewsPage")
        }
    }

    var webView by remember { mutableStateOf<WebView?>(null) }

    BackHandler {
        if (webView?.canGoBack() == true) webView?.goBack()
        else navigationBack()
    }


    AndroidView(factory = { context ->
        WebView(context).apply {
//            layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )

            webViewClient = MyWebViewClient {}

            this.settings.javaScriptEnabled = true
            this.settings.domStorageEnabled = true


//            webViewClient = WebViewClient()
            loadUrl(url)



            webView = this
        }
    } )
}

class MyWebViewClient(private val function: (String) -> Unit) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        if (url != null) {
            function(url)
        }

        Log.d("!!", "onPageStarted: ")
        super.onPageStarted(view, url, favicon)
    }


    // 避免預設瀏覽器開啟連結
    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.shouldOverrideUrlLoading(view, url)",
            "android.webkit.WebViewClient"
        )
    )
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }
}