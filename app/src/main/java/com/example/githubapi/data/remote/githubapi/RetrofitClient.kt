package com.example.githubapi.data.remote.githubapi

import android.util.Log
import com.example.githubapi.data.BASE_URL
import com.example.githubapi.data.GITHUB_TOKEN
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

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
        val response = apiServiceGson.searchRepositories("Kotlin")
//        val call = apiServiceGson.searchRepositories(GITHUB_TOKEN, "Kotlin")
//        val exc = call.execute()
        if (response.isSuccessful) {
            val list = response.body()
            list?.items?.forEach {
                Log.d("api", it.toString())
            }
        }
    }

    fun searchRepositories() {

    }

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

