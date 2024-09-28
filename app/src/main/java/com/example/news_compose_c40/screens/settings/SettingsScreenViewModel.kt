package com.example.news_compose_c40.screens.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    initialLanguage: Pair<String, String>,
) : ViewModel() {

    private val _selectedLanguage = mutableStateOf(initialLanguage.second)
    val selectedLanguage: String get() = _selectedLanguage.value

    private val _selectedLanguageCode =
        mutableStateOf(initialLanguage.first) // Store the language code
    val selectedLanguageCode: String get() = _selectedLanguageCode.value
    
    fun updateLanguage(newLanguage: String, newLanguageCode: String) {
        _selectedLanguage.value = newLanguage
        _selectedLanguageCode.value = newLanguageCode
    }
}