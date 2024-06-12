package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoHistorialItem
import javax.inject.Inject

class RegistrarTerminadosUseCase @Inject constructor(
    private val repository: PillboxDbRepository
) {
    suspend fun invoke() {
        repository.getMedicamentosTerminados().forEach {
            repository.addMedicamentoHistorial(
                MedicamentoHistorialItem(
                    fkMedicamento = it.fkMedicamento,
                    fkMedicamentoActivo = it,
                    fkUsuario = it.fkUsuario
                )
            )
        }
    }
}