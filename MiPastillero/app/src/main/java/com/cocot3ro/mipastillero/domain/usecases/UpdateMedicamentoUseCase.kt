package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.PillboxDbRepository
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem
import javax.inject.Inject

class UpdateMedicamentoUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository,
    private val cancelarNotificacionesUseCase: CancelarNotificacionesUseCase,
    private val programarNotificacionesUseCase: ProgramarNotificacionesUseCase
) {
    suspend fun invoke(medicamento: MedicamentoItem) {
        pillboxDbRepository.updateMedicamento(medicamento)
    }

    suspend fun invoke(medicamentoActivo: MedicamentoActivoItem) {
        pillboxDbRepository.updateMedicamentoActivo(medicamentoActivo)
        cancelarNotificacionesUseCase.invoke(medicamentoActivo)
        programarNotificacionesUseCase.invoke(medicamentoActivo)
    }

    suspend fun invoke(oldCodNacional: Long, newMed: MedicamentoItem) {
        pillboxDbRepository.updateCodNacional(oldCodNacional, newMed.pkCodNacionalMedicamento)
        pillboxDbRepository.updateMedicamento(newMed)
    }
}