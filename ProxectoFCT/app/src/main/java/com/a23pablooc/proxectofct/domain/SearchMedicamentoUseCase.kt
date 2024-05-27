package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.data.PillboxRepository
import javax.inject.Inject

class SearchMedicamentoUseCase @Inject constructor(
    private val repository: PillboxRepository
) {
    suspend operator fun invoke(codNacional: String) = repository.searchMedicamento(codNacional)
}
