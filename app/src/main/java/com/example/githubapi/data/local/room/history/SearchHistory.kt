package com.example.githubapi.data.local.room.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "search_term")
    val searchTerm: String,
    @ColumnInfo(name = "last_used")
    val lastUsed: Long
)