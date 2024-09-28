package com.example.news_compose_c40.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.text.intl.Locale
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {
    @Provides
    fun provideInitialLanguage(): Pair<String, String> {
        val languages = mapOf("en" to "English", "ar" to "العربية")
        val languageCode = Locale.current.language
        val systemLanguage = languages[languageCode] ?: "English"
        val appLanguage =
            AppCompatDelegate.getApplicationLocales().get(0)?.toLanguageTag() ?: languageCode
        val appDisplayName = languages[appLanguage] ?: systemLanguage

        return Pair(appLanguage, appDisplayName)
    }
}