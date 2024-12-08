package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.MiPastilleroDbRepository
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem
import javax.inject.Inject

class UpdateMedicamentoUseCase @Inject constructor(
    private val miPastilleroDbRepository: MiPastilleroDbRepository,
    private val cancelarNotificacionesUseCase: CancelarNotificacionesUseCase,
    private val programarNotificacionesUseCase: ProgramarNotificacionesUseCase
) {
    suspend fun invoke(medicamento: MedicamentoItem) {
        miPastilleroDbRepository.updateMedicamento(medicamento)
    }

    suspend fun invoke(medicamentoActivo: MedicamentoActivoItem) {
        miPastilleroDbRepository.updateMedicamentoActivo(medicamentoActivo)
        cancelarNotificacionesUseCase.invoke(medicamentoActivo)
        programarNotificacionesUseCase.invoke(medicamentoActivo)
    }

    suspend fun invoke(oldCodNacional: Long, newMed: MedicamentoItem) {
        miPastilleroDbRepository.updateCodNacional(oldCodNacional, newMed.pkCodNacionalMedicamento)
        miPastilleroDbRepository.updateMedicamento(newMed)
    }
}