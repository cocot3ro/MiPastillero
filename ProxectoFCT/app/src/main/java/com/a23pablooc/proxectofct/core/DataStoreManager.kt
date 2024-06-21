package com.a23pablooc.proxectofct.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val preferences: DataStore<Preferences>
) {
    companion object {
        const val PREFERENCES_NAME = "preferences"
    }

    object PreferencesKeys {
        const val USE_IMAGES = "use_images"
        const val USE_HIGH_QUALITY_IMAGES = "use_high_quality_images"
        const val USE_NOTIFICATIONS = "use_notifications"

        const val DEFAULT_USER_ID = "default_user_id"
        const val USER_TO_LOGIN_ID = "user_to_login_id"
    }

    private object Keys {
        val USE_IMAGES = booleanPreferencesKey(PreferencesKeys.USE_IMAGES)
        val USE_HIGH_QUALITY_IMAGES = booleanPreferencesKey(PreferencesKeys.USE_HIGH_QUALITY_IMAGES)
        val USE_NOTIFICATIONS = booleanPreferencesKey(PreferencesKeys.USE_NOTIFICATIONS)

        val DEFAULT_USER_ID = longPreferencesKey(PreferencesKeys.DEFAULT_USER_ID)
        val USER_TO_LOGIN_ID = longPreferencesKey(PreferencesKeys.USER_TO_LOGIN_ID)
    }

    object Defaults {
        const val USE_IMAGES = true
        const val USE_HIGH_QUALITY_IMAGES = true
        const val USE_NOTIFICATIONS = true

        const val DEFAULT_USER_ID = 0L
    }

    suspend fun setUpUserToLoginId(userId: Long) = preferences.edit {
        it[Keys.USER_TO_LOGIN_ID] = userId
    }

    suspend fun getUserToLoginId(): Long? {
        return preferences.data
            .map { it[Keys.USER_TO_LOGIN_ID] ?: Defaults.DEFAULT_USER_ID }
            .first()
            .takeIf { it != Defaults.DEFAULT_USER_ID }
            .also { setUpUserToLoginId(Defaults.DEFAULT_USER_ID) }
    }

    fun useImages() = preferences.data.map {
        it[Keys.USE_IMAGES] ?: Defaults.USE_IMAGES
    }

    private suspend fun useImages(value: Boolean) {
        preferences.edit { it[Keys.USE_IMAGES] = value }
    }

    fun useHighQualityImages() = preferences.data.map {
        it[Keys.USE_HIGH_QUALITY_IMAGES] ?: Defaults.USE_HIGH_QUALITY_IMAGES
    }

    private suspend fun useHighQualityImages(value: Boolean) {
        preferences.edit { it[Keys.USE_HIGH_QUALITY_IMAGES] = value }
    }

    fun useNotifications() = preferences.data.map {
        it[Keys.USE_NOTIFICATIONS] ?: Defaults.USE_NOTIFICATIONS
    }

    private suspend fun useNotifications(value: Boolean) {
        preferences.edit { it[Keys.USE_NOTIFICATIONS] = value }
    }

    fun defaultUserId() = preferences.data.map {
        it[Keys.DEFAULT_USER_ID] ?: Defaults.DEFAULT_USER_ID
    }

    suspend fun defaultUserId(value: Long) {
        preferences.edit { it[Keys.DEFAULT_USER_ID] = value }
    }

    suspend fun saveSettings(mapSettings: MutableMap<String, *>) {
        mapSettings.forEach { (k, v) ->
            when (k) {
                PreferencesKeys.USE_IMAGES -> useImages(v as Boolean)
                PreferencesKeys.USE_HIGH_QUALITY_IMAGES -> useHighQualityImages(v as Boolean)
                PreferencesKeys.USE_NOTIFICATIONS -> useNotifications(v as Boolean)
            }
        }
    }

    suspend fun loadSettings(): MutableMap<String, *> {
        return mutableMapOf<String, Any>().apply {
            this[PreferencesKeys.USE_IMAGES] = useImages().first()
            this[PreferencesKeys.USE_HIGH_QUALITY_IMAGES] = useHighQualityImages().first()
            this[PreferencesKeys.USE_NOTIFICATIONS] = useNotifications().first()
        }
    }

    suspend fun clear() {
        preferences.edit {
            it.clear()
        }
    }
}