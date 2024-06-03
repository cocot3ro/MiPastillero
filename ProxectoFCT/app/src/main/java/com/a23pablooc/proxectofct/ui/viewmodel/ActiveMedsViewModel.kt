package com.a23pablooc.proxectofct.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.core.InternalStorageDefinitions
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.usecases.AddMedicamentoActivoUseCase
import com.a23pablooc.proxectofct.domain.usecases.CopyImageUseCase
import com.a23pablooc.proxectofct.domain.usecases.GetMedicamentosActivosUseCase
import com.a23pablooc.proxectofct.domain.usecases.UpdateMedicamentoUseCase
import com.a23pablooc.proxectofct.ui.view.states.MainScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ActiveMedsViewModel @Inject constructor(
    private val getMedicamentosActivosUseCase: GetMedicamentosActivosUseCase,
    private val addMedicamentoActivoUseCase: AddMedicamentoActivoUseCase,
    private val updateMedicamentoUseCase: UpdateMedicamentoUseCase,
    private val copyImageUseCase: CopyImageUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainScreenUiState> =
        MutableStateFlow(MainScreenUiState.Loading)

    val uiState: StateFlow<MainScreenUiState> = _uiState

    // TODO: Hardcode string
    fun fetchData() {
        viewModelScope.launch {
            getMedicamentosActivosUseCase.invoke()
                .catch {
                    _uiState.value = MainScreenUiState
                        .Error("Error fetching active meds from DB", it)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = MainScreenUiState.Success(it)
                }
        }
    }

    fun addActiveMed(context: Context, med: MedicamentoActivoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val root = context.filesDir.absolutePath
            val tempFolder = InternalStorageDefinitions.TEMP_FOLDER
            val imagesFolder = InternalStorageDefinitions.IMAGES_FOLDER

            val imgPath = med.fkMedicamento.imagen

            if (imgPath.startsWith("$root/$tempFolder")) {
                val codNacional = addMedicamentoActivoUseCase.invoke(med)

                val imageFile = File(context.filesDir, imgPath.substringAfter("$root/"))
                val folderWhereSave = File(context.filesDir, imagesFolder)

                val copiedPath = copyImageUseCase.invoke(imageFile, folderWhereSave, "$codNacional.jpg")

                med.fkMedicamento.imagen = copiedPath
                updateMedicamentoUseCase.invoke(med.fkMedicamento)
            }
        }
    }
}