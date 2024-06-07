package com.a23pablooc.proxectofct.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val preferences: DataStore<Preferences>
) {
    companion object {
        const val PREFERENCES_NAME = "preferences"

        object Keys {
            val USE_IMAGES = booleanPreferencesKey("use_images")
            val USE_HIGH_QUALITY_IMAGES = booleanPreferencesKey("use_high_quality_images")
            val DEFAULT_USER_ID = longPreferencesKey("default_user_id")
        }

        private object Defaults {
            const val USE_IMAGES = true
            const val USE_HIGH_QUALITY_IMAGES = true
            const val DEFAULT_USER_ID = 0L
        }
    }

    fun useImages() = preferences.data.map { it[Keys.USE_IMAGES] ?: Defaults.USE_IMAGES }
    suspend fun useImages(value: Boolean) {
        preferences.edit { it[Keys.USE_IMAGES] = value }
    }

    fun useHighQualityImages() = preferences.data.map {
        it[Keys.USE_HIGH_QUALITY_IMAGES] ?: Defaults.USE_HIGH_QUALITY_IMAGES
    }

    suspend fun useHighQualityImages(value: Boolean) {
        preferences.edit { it[Keys.USE_HIGH_QUALITY_IMAGES] = value }
    }

    fun defaultUserId() =
        preferences.data.map { it[Keys.DEFAULT_USER_ID] ?: Defaults.DEFAULT_USER_ID }
    suspend fun defaultUserId(value: Long) {
        preferences.edit { it[Keys.DEFAULT_USER_ID] = value }
    }
    fun hasDefaultUserId() = preferences.data.map { it.contains(Keys.DEFAULT_USER_ID) }
}