package com.example.githubapi.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.githubapi.data.local.SettingsDataStore
import com.example.githubapi.data.local.dataStore
import com.example.githubapi.data.local.room.AppDatabase
import com.example.githubapi.data.local.room.SearchHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//    @Provides
//    @Singleton
//    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
//        return context.dataStore
//    }
//}
//
//
//@Module
//@InstallIn(ViewModelComponent::class)
//object ViewModelModule {
//
//    @Provides
//    fun provideSettingsDataStore(
//        dataStore: DataStore<Preferences>
//    ): SettingsDataStore {
//        return SettingsDataStore(dataStore)
//    }
//
//    @Provides
//    fun provideMainViewModel(
//        settingsDataStore: SettingsDataStore
//    ): MainViewModel {
//        return MainViewModel(settingsDataStore)
//    }
//}
//
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DataStoreModule {
//
//    @Provides
//    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
//        context.dataStore
//
//}



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
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "search_history_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSearchHistoryDao(appDatabase: AppDatabase): SearchHistoryDao {
        return appDatabase.searchHistoryDao()
    }
}