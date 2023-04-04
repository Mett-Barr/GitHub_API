package com.example.githubapi.data.local.room.history

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchHistory::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}