package com.example.githubapi.data.local.room.history

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    // get
    @Query("SELECT * FROM search_history ORDER BY last_used DESC")
    suspend fun getAll(): List<SearchHistory>
    @Query("SELECT * FROM search_history ORDER BY last_used DESC")
    fun getAllFlow(): Flow<List<SearchHistory>>

    @Query("SELECT * FROM search_history ORDER BY last_used DESC")
    fun getAllLiveData(): LiveData<List<SearchHistory>>


    // search
    @Query("SELECT * FROM search_history WHERE search_term = :searchTerm LIMIT 1")
    suspend fun getSearchHistoryBySearchTerm(searchTerm: String): SearchHistory?


    //
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchHistory: SearchHistory)

    @Query("DELETE FROM search_history WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM search_history")
    suspend fun deleteAll()

    @Update
    suspend fun update(searchHistory: SearchHistory)
}