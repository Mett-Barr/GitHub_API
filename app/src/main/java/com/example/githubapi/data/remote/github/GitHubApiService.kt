package com.example.githubapi.data.remote.github

import com.example.githubapi.data.remote.github.getrepo.json.GetRepoItem
import com.example.githubapi.data.remote.github.search.repositories.GitHubRepositories
import com.example.githubapi.data.remote.github.search.user.GitHubUsers
import com.example.githubapi.data.remote.github.users.GitHubUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Header("Accept") accept: String = "application/vnd.github+json",
        @Header("Authorization") authHeader: String? = "Bearer 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",
        @Query("q") query: String,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
        @Query("per_page") perPage: Int? = 30,
        @Query("page") page: Int? = null
    ): Response<GitHubRepositories>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<GetRepoItem>

//    @GET("users/{username}")
//    suspend fun getUser(@Path("username") username: String): Call<GitHubUsers>

//    @GET("users/{username}")
//    suspend fun getUser(@Path("username") username: String): Response<GitHubUsers>


    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null
    ): Response<GitHubUsers>


//    @GET("users/{username}")
//    suspend fun getUser(
//        @Header("accept") accept: String = "application/vnd.github+json",
//        @Path("username") username: String
//    ): Response<GitHubUser>
}

enum class Oauth(private val description: String) {
    ENABLE("on"), UNABLE("off");

    override fun toString(): String {
        return description
    }

    fun getValue() = when (this) {
        ENABLE -> true
        UNABLE -> false
    }
}

enum class Sort(private val description: String) {
    BEST_MATCH("best match"),
    STARS("stars"),
    FORKS("forks"),
    HELP_WANTED_ISSUES("help wanted issues"),
    UPDATED("updated");

    override fun toString(): String {
        return description
    }

    fun getValue() = when (this) {
        BEST_MATCH -> null
        STARS -> description
        FORKS -> description
        HELP_WANTED_ISSUES -> "help-wanted-issues"
        UPDATED -> description
    }
}

enum class Order(private val description: String) {
    DESC("descend"), ASC("ascend");

    override fun toString(): String {
        return description
    }

    fun getValue() = when (this) {
        DESC -> "desc"
        ASC -> "asc"
    }
}

enum class PerPage(private val description: String) {
    PAGE_10("10"), PAGE_30("30"), PAGE_50("50"), PAGE_100("100"), ;

    override fun toString(): String {
        return description
    }

    fun getValue() = description.toInt()
}