package com.example.githubapi.data.remote.github.search.user

data class GitHubUsers(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)