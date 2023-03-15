package com.example.githubapi.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

//private val IS_COMPOSE_MODE = booleanPreferencesKey("is_compose_mode")
//
//class SettingsDataStore (private val dataStore: DataStore<Preferences>) {
//
//    suspend fun getComposeMode(): Boolean {
//        val preferences = dataStore.data.first()
//        return preferences[IS_COMPOSE_MODE] ?: false
//    }
//
//    suspend fun saveComposeMode(isComposeMode: Boolean) {
//        dataStore.edit { preferences ->
//            preferences[IS_COMPOSE_MODE] = isComposeMode
//        }
//    }
//}
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DataStoreModule {
//
//    private const val DATA_STORE_NAME = "my_data_store"
//
//    @Provides
//    @Singleton
//    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
//        return context.createDataStore(DATA_STORE_NAME)
//    }
//}


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)


@Singleton
class SettingsDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val IS_COMPOSE_MODE = booleanPreferencesKey("is_compose_mode")
    }

    suspend fun getComposeMode(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[IS_COMPOSE_MODE] ?: false
    }

    suspend fun saveComposeMode(isComposeMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_COMPOSE_MODE] = isComposeMode
        }
    }
}

//    suspend fun getIsComposeMode(): Boolean {
//        return dataStore.data
//            .catch { exception ->
//                if (exception is IOException) {
//                    emit(emptyPreferences())
//                } else {
//                    throw exception
//                }
//            }
//            .map { preferences ->
//                preferences[IS_COMPOSE_MODE] ?: false
//            }
//            .first()
//    }
//
//    suspend fun setIsComposeMode(value: Boolean) {
//        dataStore.edit { preferences ->
//            preferences[IS_COMPOSE_MODE] = value
//        }
//    }
//}