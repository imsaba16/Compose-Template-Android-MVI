package com.example.baseapp.utils

import android.content.*
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferenceManager(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "cookie_fam_prefs")

    suspend fun <T> getValue(
        key: Preferences.Key<T>,
        defaultValue: T
    ): T {
        return context.dataStore.data
            .map { it[key] ?: defaultValue }
            .first()
    }

    suspend fun <T> setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    suspend fun <T> remove(
        key: Preferences.Key<T>
    ) {
        context.dataStore.edit { prefs ->
            prefs.remove(key)
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}