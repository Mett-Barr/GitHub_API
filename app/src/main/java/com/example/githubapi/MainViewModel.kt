package com.example.githubapi

import android.content.res.Resources.Theme
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubapi.data.local.SettingsDataStore
import com.example.githubapi.data.local.room.bookmark.Bookmark
import com.example.githubapi.data.local.room.bookmark.BookmarkDao
import com.example.githubapi.data.local.room.history.SearchHistory
import com.example.githubapi.data.local.room.history.SearchHistoryDao
import com.example.githubapi.data.remote.github.*
import com.example.githubapi.data.remote.github.getrepo.json.GetRepoItem
import com.example.githubapi.data.remote.github.search.repositories.Item
import com.example.githubapi.data.remote.github.search.repositories.RateLimitException
import com.example.githubapi.data.remote.github.search.repositories.RepoPagingSource
import com.example.githubapi.ui.navigation.MainRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: SettingsDataStore,
    private val searchHistoryDao: SearchHistoryDao,
    private val bookmarkDao: BookmarkDao
//    private val dataStore: SettingsDataStore
) : ViewModel() {

    /** UI State */
    // custom settings
    val isComposeMode = MutableLiveData(false)
    //    var isComposeMode by mutableStateOf(true)
//        private set
    fun onComposeModeChange(boo: Boolean) {
        isComposeMode.value = boo
        viewModelScope.launch {
            dataStore.saveComposeMode(boo)
        }
    }

    val themeMode = MutableLiveData(ThemeMode.SYSTEM_DEFAULT)
    fun onThemeModeChange(mode: ThemeMode) {
        themeMode.value = mode
        viewModelScope.launch {
            dataStore.saveThemeMode(mode.name)
        }
    }



    var isSearchBarExpanded by mutableStateOf(false)
        private set
    fun onSearchBarStateChange(boo: Boolean) {
        isSearchBarExpanded = boo
    }

    // navigation
    val route = MutableStateFlow<MainRoute>(MainRoute.Settings)

    fun navigate(route: MainRoute) {
        this.route.value = route
    }

//    fun onSearchBarStateChanged() {
//        if (!isSearchBarExpanded) {
//            // 如果 SearchBar 處於關閉狀態，處理 lastSearch
//            handleLastSearch()
//        }
//    }

    fun handleLastSearch() {
        if (lastSearch.isNotBlank()) {
            viewModelScope.launch {
                val existingSearchHistory =
                    searchHistoryDao.getSearchHistoryBySearchTerm(lastSearch)

                if (existingSearchHistory != null) {
                    // 更新現有的搜索歷史
                    val updatedSearchHistory =
                        existingSearchHistory.copy(lastUsed = System.currentTimeMillis())
                    updateSearchHistory(updatedSearchHistory)
                } else {
                    // 插入新的搜索歷史
                    insertSearchHistory(lastSearch)
                }
            }
        }
    }

//    var isMoshiMode by mutableStateOf(true)
//        private set

    init {
        viewModelScope.launch {
            isComposeMode.value = dataStore.getComposeMode()
            themeMode.value = when (dataStore.getThemeMode()) {
                ThemeMode.LIGHT.name -> ThemeMode.LIGHT
                ThemeMode.DARK.name -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM_DEFAULT
            }

//            gitRepo.clear()
//            RetrofitClient.test2()?.items?.let { gitRepo.addAll(it) }
//            gitRepo = RetrofitClient.test2()?.items


//            repo = RetrofitClient.test2()
        }
    }

    //
//    fun test() {
//        viewModelScope.launch {
//            dataStore.saveComposeMode(!isComposeMode)
//        }
//    }
    fun test() {
        viewModelScope.launch {
//            isComposeMode = !isComposeMode
//            dataStore.saveComposeMode(isComposeMode)
        }
    }


    /** UI Data */
    var gitRepo = mutableStateListOf<Item>()

    //    var repo by mutableStateOf<GitHubRepositories?>(null)
    var pagingData: Flow<PagingData<Item>> = emptyFlow()
        private set
//    var isOauthEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
//        private set

    // search param
    val queryFlow = MutableStateFlow("Kotlin")
    var oauth: MutableLiveData<Oauth> = MutableLiveData(Oauth.UNABLE)
        private set
    var sort: MutableLiveData<Sort> = MutableLiveData(Sort.BEST_MATCH)
        private set
    var order: MutableLiveData<Order> = MutableLiveData(Order.DESC)
        private set
    var perPage: MutableLiveData<PerPage> = MutableLiveData(PerPage.PAGE_30)
        private set

    var lastSearch by mutableStateOf("Kotlin")

    var url = MutableStateFlow("")


    fun searchRepo(
    ) {

        if (queryFlow.value.isNotBlank()) {
//            Log.d("!!!", "searchRepo: text = ${queryFlow.value}")

            pagingData = Pager(
                config = PagingConfig(pageSize = perPage.value?.getValue() ?: 30),
//            config = PagingConfig(pageSize = perPage),
                pagingSourceFactory = {
                    RepoPagingSource(
                        isOauthEnabled = oauth.value?.getValue(),
                        query = queryFlow.value,
                        sort = sort.value?.getValue(),
                        order = order.value?.getValue(),
                        perPage = perPage.value?.getValue(),
//                    isOauthEnabled = isUsingOauth,
//                    query = searchTextFlow.value,
//                    sort = sort,
//                    order = order,
//                    perPage = perPage,
                    )

//                repoPagingSource.value!!
                }
            ).flow.retry { throwable ->
                if (throwable is RateLimitException) {
                    delay(throwable.retryDelay * 1000L)
                    true
                } else {
                    false
                }
            }.cachedIn(viewModelScope)

//            lastSearch = queryFlow.value
//            handleLastSearch()
        }
    }

    fun searchRepoAndStore() {
        if (queryFlow.value.isNotBlank()) {
            searchRepo()
            lastSearch = queryFlow.value
            handleLastSearch()
        }
    }

    fun backToLastSearch() {
        if (lastSearch.isNotBlank()) {
            queryFlow.value = lastSearch
        }
    }


    // testing

    var isUsingOauth by mutableStateOf(false)

//    val repoPagingSource = mutableStateOf<RepoPagingSource?>(null)

    var isRateLimited by mutableStateOf(false)
    private suspend fun getRepo(page: Int): List<Item>? {
        return RetrofitClient.test2(page)?.items
    }

    var textFieldValue by mutableStateOf(TextFieldValue("Kotlin"))


//    fun invaTest() {
////        repoPagingSource.value?.invalidate()
////        searchRepo()
//
//    }

//    private suspend fun getRepo2(page: Int): Response<GitHubRepositories> {
//        val response = RetrofitClient.pagingTest(page)
//
//        Log.d("response", response.code().toString() + "  " + response.toString())
//
//        // 如果响应状态不是成功，则检查速率限制
//        if (!response.isSuccessful) {
//            // 如果响应状态是 429（Too Many Requests），则抛出 RateLimitException
//            if (response.code() == 403) {
////            if (response.code() == 429) {
//                val resetTime = response.headers().get("X-RateLimit-Reset")?.toLongOrNull() ?: 0L
//                val currentTime = System.currentTimeMillis() / 1000L
//                val retryDelay = max(resetTime - currentTime, 0L)
//
//                isRateLimited = true
//
//                Log.d("response", "getRepo2: isRateLimited $resetTime  $retryDelay")
//
//                throw RateLimitException(retryDelay)
//            } else {
//                throw HttpException(response)
//            }
//        }
//
//        return response
//    }

//    val pagingData: Flow<PagingData<Item>> = Pager(
//        config = PagingConfig(pageSize = 30),
//        pagingSourceFactory = {
//            RepoPagingSource(::getRepo, ::getRepo2)
//        }
//    ).flow.retry { throwable ->
//        if (throwable is RateLimitException) {
//            delay(throwable.retryDelay * 1000L)
//            true
//        } else {
//            false
//        }
//    }


//    val status = mutableStateOf(Status.IDLE)
//    enum class Status {
//        IDLE, LOADING, SUCCESS, ERROR
//    }

//    val pagingItems = _pagingData.collectAsLazyPagingItems(viewModelScope)


    init {
        searchRepo()
    }


    /** Room */
    val searchHistories: LiveData<List<SearchHistory>> = searchHistoryDao.getAllLiveData()

    suspend fun getAllSearchHistories() = searchHistoryDao.getAll()

    fun insertSearchHistory(searchTerm: String) = viewModelScope.launch {
        val searchHistory =
            SearchHistory(searchTerm = searchTerm, lastUsed = System.currentTimeMillis())
        searchHistoryDao.insert(searchHistory)
    }

    fun deleteSearchHistory(id: Int) = viewModelScope.launch {
        searchHistoryDao.delete(id)
    }

    fun deleteAllSearchHistories() = viewModelScope.launch {
        searchHistoryDao.deleteAll()
    }

    fun updateSearchHistory(searchHistory: SearchHistory) = viewModelScope.launch {
        searchHistoryDao.update(searchHistory)
    }

    // bookmark
    val bookmarks: LiveData<List<Bookmark>> = bookmarkDao.getAllBookmarks()
    private val repoCache = mutableMapOf<String, GetRepoItem?>()

    fun isBookmarkExists(fullName: String): LiveData<Boolean> {
        return bookmarks.map { bookmarksList ->
            bookmarksList.any { it.fullName == fullName }
        }
    }

    suspend fun getRepo(fullName: String): GetRepoItem? {
        // 如果缓存中存在此仓库，直接从缓存获取
        if (repoCache.containsKey(fullName)) {
            return repoCache[fullName]
        } else {
            return try {
                val response = RetrofitClient.getUser(fullName)
                if (response.isSuccessful) {
                    // 将新获取的数据添加到缓存中
                    repoCache[fullName] = response.body()
                    Log.d("!!!", "getRepo: Successful request. Response body: ${response.body()}")
                    response.body()
                } else {
                    Log.d(
                        "!!!",
                        "getRepo: Request failed. Response code: ${response.code()}, Response message: ${response.message()}"
                    )
                    null
                }
            } catch (e: Exception) {
                Log.d("!!!", "getRepo: Exception occurred: ${e.message}")
                null
            }
        }
    }

    suspend fun getAllBookmarks() = bookmarkDao.getAllBookmarks()

    fun insertBookmark(fullName: String) = viewModelScope.launch {
        val bookmark = Bookmark(fullName = fullName)
        bookmarkDao.insertBookmark(bookmark)
    }

    fun deleteBookmark(id: Int) = viewModelScope.launch {
        bookmarkDao.deleteBookmarkById(id)
    }

    fun deleteBookmarkByFullName(fullName: String) = viewModelScope.launch {
        bookmarkDao.deleteByFullName(fullName)
    }

    fun deleteAllBookmarks() = viewModelScope.launch {
        bookmarkDao.deleteAllBookmarks()
    }

    suspend fun getBookmarkById(bookmarkId: Int) = bookmarkDao.getBookmarkById(bookmarkId)

//    suspend fun getBookmarkByUrl(url: String) = bookmarkDao.getBookmarkByUrl(url)


    init {

    }
}

enum class ThemeMode{
    LIGHT, DARK, SYSTEM_DEFAULT
}


//class RateLimitException(val retryDelay: Long) :
//    IOException("Rate limit exceeded. Retry after $retryDelay seconds.")

