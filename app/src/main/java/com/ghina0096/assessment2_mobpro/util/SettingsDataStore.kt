package com.ghina0096.assessment2_mobpro.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_preference")

class SettingsDataStore(private val context: Context) {
    companion object {
        private val IS_LIST = booleanPreferencesKey("is_list")
        private val THEME_COLOR = intPreferencesKey("theme_color")
    }

    val layoutFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LIST] ?: true
    }

    val colorFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[THEME_COLOR] ?: Color.White.hashCode()
    }

    suspend fun saveLayout(isList: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LIST] = isList
        }
    }

    suspend fun saveThemeColor(colorInt: Int) {
        context.dataStore.edit { preferences ->
            preferences[THEME_COLOR] = colorInt
        }
    }
}
