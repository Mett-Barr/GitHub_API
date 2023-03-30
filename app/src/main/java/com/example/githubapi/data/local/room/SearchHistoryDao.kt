package com.example.githubapi.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SearchHistoryDao {
    @Insert
    suspend fun insert(searchHistory: SearchHistory)

    @Query("SELECT * FROM search_history")
    suspend fun getAll(): List<SearchHistory>
}