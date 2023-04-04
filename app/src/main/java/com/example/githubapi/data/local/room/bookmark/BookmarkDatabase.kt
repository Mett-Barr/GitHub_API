package com.example.githubapi.data.local.room.bookmark

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bookmark::class], version = 1)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}