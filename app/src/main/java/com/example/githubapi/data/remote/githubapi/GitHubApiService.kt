package com.example.githubapi.data.remote.githubapi

import com.example.githubapi.data.remote.githubapi.repositories.GitHubRepositories
import com.example.githubapi.data.remote.githubapi.user.GitHubUser
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("search/repositories")
    suspend fun searchRepositories(
//        @Header("Authorization") token: String,
        @Query("q") query: String
    ): Response<GitHubRepositories>

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Call<GitHubUser>


//    data class Repo(
//        @SerializedName("name") val name: String,
//        @SerializedName("full_name") val fullName: String,
//        @SerializedName("html_url") val htmlUrl: String
//    )
//
//        @GET("search/repositories")
//        fun searchRepositories2(
//            @Header("Authorization") token: String,
//            @Query("q") query: String
//        ): Call<RepoSearchResponse>
//
//    data class RepoSearchResponse(
//        @SerializedName("items") val items: List<Repo>
//    )
}