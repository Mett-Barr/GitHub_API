package com.example.githubapi.data.local.room.bookmark

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark): Long

    @Query("DELETE FROM bookmark WHERE id = :bookmarkId")
    suspend fun deleteBookmarkById(bookmarkId: Int)

    @Query("DELETE FROM bookmark WHERE full_name = :fullName")
    suspend fun deleteByFullName(fullName: String)

    @Query("SELECT * FROM bookmark")
    fun getAllBookmarks(): LiveData<List<Bookmark>>

    @Query("SELECT * FROM bookmark WHERE id = :bookmarkId")
    suspend fun getBookmarkById(bookmarkId: Int): Bookmark?

    @Query("DELETE FROM bookmark")
    suspend fun deleteAllBookmarks()
}