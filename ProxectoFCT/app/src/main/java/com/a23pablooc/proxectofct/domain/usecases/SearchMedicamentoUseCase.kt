package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.repositories.CimaRepository
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import javax.inject.Inject

class SearchMedicamentoUseCase @Inject constructor(
    private val cimaRepository: CimaRepository,
    private val pillboxDbRepository: PillboxDbRepository,
    private val userInfoProvider: UserInfoProvider
) {
    suspend fun invoke(codNacional: Long): MedicamentoItem? {
        return pillboxDbRepository.findMedicamentoByCodNacional(
            userInfoProvider.currentUser!!.pkUsuario,
            codNacional
        ) ?: cimaRepository.searchMedicamento(codNacional)
    }
}
