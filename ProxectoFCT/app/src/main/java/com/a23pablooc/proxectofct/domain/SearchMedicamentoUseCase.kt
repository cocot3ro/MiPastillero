package com.a23pablooc.proxectofct.domain

import android.util.Log
import com.a23pablooc.proxectofct.data.repositories.CimaRepository
import com.a23pablooc.proxectofct.data.repositories.PillboxRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import javax.inject.Inject

class SearchMedicamentoUseCase @Inject constructor(
    private val cimaRepository: CimaRepository,
    private val pillboxRepository: PillboxRepository
) {
    suspend operator fun invoke(codNacional: Int): MedicamentoItem? {
        val dbMed = pillboxRepository.findMedicamentoByCodNacional(codNacional)

        if (dbMed != null) {
            return dbMed.also { Log.d("SearchMedicamentoUseCase", "Medicamento found in database") }
        }

        val apiMed = cimaRepository.searchMedicamento(codNacional)

        if (apiMed != null) {
            return apiMed.also { Log.d("SearchMedicamentoUseCase", "Medicamento found in API")}
        }

        Log.d("SearchMedicamentoUseCase", "Medicamento not found")

        return null
    }
}
