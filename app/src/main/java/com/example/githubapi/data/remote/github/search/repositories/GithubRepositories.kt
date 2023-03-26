package com.example.githubapi.data.remote.github.search.repositories

data class GitHubRepositories(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)

//interface RepositoriesApiService {
//    @GET("search/repositories")
//    fun searchRepositories(@Query("q") query: String): Call<GitHubRepositories>
//}