package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.data.repositories.CimaRepository
import com.a23pablooc.proxectofct.data.repositories.PillboxRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import javax.inject.Inject

class SearchMedicamentoUseCase @Inject constructor(
    private val cimaRepository: CimaRepository,
    private val pillboxRepository: PillboxRepository
) {
    suspend operator fun invoke(codNacional: String): MedicamentoItem {
        return cimaRepository.searchMedicamento(codNacional).apply {
            pillboxRepository.findFavoritoByCodNacional(codNacional)
        }
    }
}
