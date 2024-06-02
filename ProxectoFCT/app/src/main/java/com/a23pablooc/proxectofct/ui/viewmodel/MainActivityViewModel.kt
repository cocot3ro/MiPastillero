package com.a23pablooc.proxectofct.ui.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.core.DateTimeUtils.zero
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import com.a23pablooc.proxectofct.data.database.dao.UsuarioDAO
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.domain.model.extensions.toDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    /*private val checkFinishedUseCase: CheckFinishedUseCase*/
    private val usuarioDAO: UsuarioDAO,
    private val medicamentoDAO: MedicamentoDAO,
    private val medicamentoActivoDAO: MedicamentoActivoDAO
) : ViewModel() {
    // TODO: Implement the ViewModel and inject the use cases

    fun checkFinished() {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: checkFinishedUseCase()
        }
    }

    @Deprecated("This is an example.")
    fun example(currentUser: UsuarioItem) {
        viewModelScope.launch(Dispatchers.IO) {
            usuarioDAO.insert(currentUser.toDatabase())

            val medicamento = MedicamentoEntity(
                afectaConduccion = false,
                numRegistro = "00123456",
                prescripcion = "No precisa prescripción",
                url = "https://www.aemps.gob.es/cima",
                esFavorito = false,
                laboratorio = "Laboratorio",
                prospecto = "https://www.aemps.gob.es/cima",
                nombre = "Paracetamol",
                imagen = byteArrayOf(),
                fkUsuario = 1,
                alias = "Paracetamol",
                pkCodNacionalMedicamento = 123456,
            )
            medicamentoDAO.insert(medicamento)

            val medicamentoActivo = MedicamentoActivoEntity(
                fkUsuario = 1,
                fechaFin = Calendar.getInstance().apply {
                    time = time.zeroTime()
                    set(2024, 5, 20)
                }.time,
                fechaInicio = Calendar.getInstance().apply {
                    time = time.zeroTime()
                    set(2024, 5, 10)
                }.time,
                dosis = "1 comprimido",
                horario = mutableSetOf(Calendar.getInstance().apply {
                    time = time.zero()
                    set(Calendar.HOUR_OF_DAY, 8)
                    set(Calendar.MINUTE, 0)
                }.time),
                pkMedicamentoActivo = 0,
                fkMedicamento = 123456,
                tomas = mutableMapOf()
            )
            medicamentoActivoDAO.insert(medicamentoActivo)
        }
    }
}