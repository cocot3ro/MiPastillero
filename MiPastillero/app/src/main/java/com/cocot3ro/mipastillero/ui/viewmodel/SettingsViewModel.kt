package com.cocot3ro.mipastillero.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocot3ro.mipastillero.core.DataStoreManager
import com.cocot3ro.mipastillero.domain.usecases.DeleteDataUseCase
import com.cocot3ro.mipastillero.domain.usecases.SaveSettingsUseCase
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