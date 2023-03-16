package com.example.githubapi.data.remote.githubapi.user

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

data class GitHubUser(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)

//interface UserApiService {
//    @GET("users/{username}")
//    fun getUser(@Path("username") username: String): Call<GitHubUser>
//}