package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.repositories.CimaRepository
import com.a23pablooc.proxectofct.data.repositories.PillboxRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import javax.inject.Inject

class SearchMedicamentoUseCase @Inject constructor(
    private val cimaRepository: CimaRepository,
    private val pillboxRepository: PillboxRepository
) {
    suspend operator fun invoke(codNacional: Int): MedicamentoItem? {
        return pillboxRepository.findMedicamentoByCodNacional(
            UserInfoProvider.currentUser.pkUsuario,
            codNacional
        ) ?: cimaRepository.searchMedicamento(codNacional)
    }
}
