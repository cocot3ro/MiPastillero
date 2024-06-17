package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.core.DataStoreManager
import com.a23pablooc.proxectofct.domain.usecases.DeleteDataUseCase
import com.a23pablooc.proxectofct.domain.usecases.SaveSettingsUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val gson: Gson,
    private val dataStoreManager: DataStoreManager,
    private val saveSettingUseCase: SaveSettingsUseCase,
    private val deleteDataUseCase: DeleteDataUseCase
) : ViewModel() {
    suspend fun loadSettings(): MutableMap<String, Any> {
        return dataStoreManager.loadSettings().map { (k, v) -> k to v!! }.toMap().toMutableMap()
    }

    fun saveSettings(mapSettings: MutableMap<String, *>) {
        viewModelScope.launch(Dispatchers.IO) {
            saveSettingUseCase.invoke(mapSettings)
        }
    }

    fun deleteData() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteDataUseCase.invoke()
        }
    }
}