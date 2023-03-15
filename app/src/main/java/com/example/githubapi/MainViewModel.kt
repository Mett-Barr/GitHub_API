package com.example.githubapi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapi.data.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: SettingsDataStore
//    private val dataStore: SettingsDataStore
) : ViewModel() {

    var isComposeMode by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            isComposeMode = dataStore.getComposeMode()
        }
    }
//
//    fun test() {
//        viewModelScope.launch {
//            dataStore.saveComposeMode(!isComposeMode)
//        }
//    }
    fun test() {
        viewModelScope.launch {
            isComposeMode = !isComposeMode
            dataStore.saveComposeMode(isComposeMode)
        }
    }
}

