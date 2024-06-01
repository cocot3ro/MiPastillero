package com.a23pablooc.proxectofct.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val preferences: DataStore<Preferences>) {

    companion object {
        const val PREFERENCES_NAME = "preferences"

        object Keys {
            val USE_IMAGES = booleanPreferencesKey("use_images")
            val USE_HIGH_QUALITY_IMAGES = booleanPreferencesKey("use_high_quality_images")
        }
    }

    fun useImages() = preferences.data.map { it[Keys.USE_IMAGES] ?: true }
    suspend fun useImages(value: Boolean) = preferences.edit { it[Keys.USE_IMAGES] = value }

    fun useHighQualityImages() = preferences.data.map { it[Keys.USE_HIGH_QUALITY_IMAGES] ?: true }
    suspend fun useHighQualityImages(value: Boolean) =
        preferences.edit { it[Keys.USE_HIGH_QUALITY_IMAGES] = value }
}