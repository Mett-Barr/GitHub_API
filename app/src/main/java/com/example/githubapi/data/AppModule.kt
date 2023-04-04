package com.example.githubapi.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.githubapi.data.local.SettingsDataStore
import com.example.githubapi.data.local.dataStore
import com.example.githubapi.data.local.room.bookmark.BookmarkDao
import com.example.githubapi.data.local.room.bookmark.BookmarkDatabase
import com.example.githubapi.data.local.room.history.HistoryDatabase
import com.example.githubapi.data.local.room.history.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    fun provideSettingsDataStore(dataStore: DataStore<Preferences>): SettingsDataStore {
        return SettingsDataStore(dataStore)
    }









    /** Room */
    // history
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): HistoryDatabase {
        return Room.databaseBuilder(
            context,
            HistoryDatabase::class.java, "search_history_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSearchHistoryDao(appDatabase: HistoryDatabase): SearchHistoryDao {
        return appDatabase.searchHistoryDao()
    }


    // bookmark
    @Provides
    @Singleton
    fun provideBookmarkDatabase(@ApplicationContext context: Context): BookmarkDatabase {
        return Room.databaseBuilder(
            context,
            BookmarkDatabase::class.java, "bookmark_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBookmarkDao(appDatabase: BookmarkDatabase): BookmarkDao {
        return appDatabase.bookmarkDao()
    }

}