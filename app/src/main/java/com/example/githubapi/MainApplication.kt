package com.example.githubapi

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    // for DataSotre
//    val dataStore: DataStore<Preferences> by preferencesDataStore(
//        name = "myDataStore"
//    )
}