package com.example.githubapi.data.remote.github.search.repositories

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubapi.data.remote.github.RetrofitClient
import kotlinx.coroutines.delay
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Long.max

class RepoPagingSource(
//    private val getRepo: suspend (Int) -> List<Item>?,
//    private val getRepo2: suspend (Int) -> Response<GitHubRepositories>

    private val isOauthEnabled: Boolean?,
    private val query: String,
    private val sort: String?,
    private val order: String?,
    private val perPage: Int?,

) : PagingSource<Int, Item>() {

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {

//        Log.d("!!!", "getRefreshKey: $state")

        return null
//        return state.anchorPosition
    }

    private fun rateLimitCheck(response: Response<GitHubRepositories>) {
        if (response.code() == 403) {
            val remainingRequests = response.headers()["x-ratelimit-remaining"]?.toIntOrNull() ?: -1
            val rateLimitExceeded = response.errorBody()?.string()?.contains("API rate limit exceeded") ?: false

            if (remainingRequests == 0 || rateLimitExceeded) {
                val resetTime = response.headers()["x-ratelimit-reset"]?.toLongOrNull() ?: 0L
                val currentTime = System.currentTimeMillis() / 1000L
                val retryDelay = max(resetTime - currentTime, 0L)

                throw RateLimitException(retryDelay)
            }
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val page = params.key ?: 1

//        Log.d("!!!", "load: page = $page")

        return try {
            val response = RetrofitClient.searchRepositories(query = query, page = page, isOauthEnabled = isOauthEnabled, perPage = perPage)
//            val response = RetrofitClient.pagingTest(page)

//            Log.d("!!!", response.toString())

            rateLimitCheck(response)

            if (response.isSuccessful) {
                val items = response.body()?.items ?: emptyList()
                val nextPage = if (items.isEmpty()) null else page + 1
                LoadResult.Page(items, prevKey = if (page == 1) null else page - 1, nextKey = nextPage)
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: RateLimitException) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }


//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
//        val page = params.key ?: 1
//
//        return try {
//            val getRepoWithRateLimitHandling = getRepoWithRateLimitHandling(getRepo2)
//            val items = getRepoWithRateLimitHandling(page) ?: emptyList()
//
//            LoadResult.Page(
//                data = items,
//                prevKey = if (page == 1) null else page - 1,
//                nextKey = if (items.isEmpty()) null else page + 1
//            )
//        } catch (e: IOException) {
//            LoadResult.Error(e)
//        } catch (e: HttpException) {
//            LoadResult.Error(e)
//        }
//    }
//    private fun getRepoWithRateLimitHandling(getRepo: suspend (Int) -> Response<GitHubRepositories>): suspend (Int) -> List<Item>? {
//        return { page ->
//            val response = getRepo(page)
//
//
//
//            if (response.isSuccessful) {
//                response.body()?.items
//            } else {
//                val rateLimitRemaining = response.headers()["X-RateLimit-Remaining"]?.toIntOrNull()
//                if (rateLimitRemaining == 0) {
//                    val rateLimitReset = response.headers()["X-RateLimit-Reset"]?.toLongOrNull()
//                    val retryDelay = rateLimitReset?.let { resetTime ->
//                        max(0, resetTime - System.currentTimeMillis() / 1000)
//                    } ?: 60
//
//                    delay(retryDelay * 1000L)
//                    getRepo(page).body()?.items
//                } else {
//                    null
//                }
//            }
//        }
//    }
}

class RateLimitException(val retryDelay: Long) : IOException("Rate limit exceeded. Retry after $retryDelay seconds.")
