package com.example.githubapi.data.remote.github

import android.util.Log
import com.example.githubapi.data.BASE_URL
import com.example.githubapi.data.remote.github.search.repositories.GitHubRepositories
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {

    private val retrofitMoshi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val apiServiceMoshi = retrofitMoshi.create(GitHubApiService::class.java)

    private val retrofitGson = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiServiceGson = retrofitGson.create(GitHubApiService::class.java)

//    val gsonInstance: RepositoriesApiService by lazy {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        retrofit.create(RepositoriesApiService::class.java)
//    }
//
//    val moshiInstance: RepositoriesApiService by lazy {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(MoshiConverterFactory.create())
//            .build()
//
//        retrofit.create(RepositoriesApiService::class.java)
//    }

    suspend fun test() {
        val response = apiServiceGson.searchRepositories(query = "Kotlin")
//        val call = apiServiceGson.searchRepositories(GITHUB_TOKEN, "Kotlin")
//        val exc = call.execute()
        if (response.isSuccessful) {
            val list = response.body()
            list?.items?.forEach {
                Log.d("api", it.toString())
            }
        }
    }

    suspend fun test2(page: Int = 1): GitHubRepositories? {

        Log.d("!!!", "test2: $page")

        val response = apiServiceGson.searchRepositories(query = "Kotlin", page = page)
//        val call = apiServiceGson.searchRepositories(GITHUB_TOKEN, "Kotlin")
//        val exc = call.execute()
        if (response.isSuccessful) {
            val list = response.body()
            list?.items?.forEach {
                Log.d("api", it.owner.avatar_url)
            }
        }


        // testing
        val rateLimitLimit = response.headers()["X-RateLimit-Limit"]?.toIntOrNull()
        val rateLimitRemaining = response.headers()["X-RateLimit-Remaining"]?.toIntOrNull()
        val rateLimitReset = response.headers()["X-RateLimit-Reset"]?.toLongOrNull()
        Log.d("response", "rateLimitLimit = $rateLimitLimit")
        Log.d("response", "rateLimitRemaining = $rateLimitRemaining")
        Log.d("response", "rateLimitReset = $rateLimitReset")

        return response.body()
    }

    suspend fun pagingTest(page: Int = 1): Response<GitHubRepositories> =
        apiServiceGson.searchRepositories(query = "Kotlin", page = page)


    suspend fun searchRepositories(
        isOauthEnabled: Boolean? = false,
        query: String,
        sort: String? = null,
        order: String? = null,
        perPage: Int? = null,
        page: Int? = null
    ) =
        apiServiceGson.searchRepositories(
            authHeader = if (isOauthEnabled == true) "Bearer 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" else null,
            query = query,
            sort = sort,
            order = order,
            perPage = perPage,
            page = page
        )


    fun getUser() {

    }
}

enum class ConverterType {
    GSON,
    MOSHI
}

enum class RepositorySortType {
    BEST_MATCH,
    STARS,
    FORKS,
    HELP_WANTED_ISSUES,
    UPDATED
}

