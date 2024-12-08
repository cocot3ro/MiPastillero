package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.core.UserInfoProvider
import com.cocot3ro.mipastillero.data.repositories.CimaRepository
import com.cocot3ro.mipastillero.data.repositories.MiPastilleroDbRepository
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem
import javax.inject.Inject

class SearchMedicamentoUseCase @Inject constructor(
    private val cimaRepository: CimaRepository,
    private val miPastilleroDbRepository: MiPastilleroDbRepository,
    private val userInfoProvider: UserInfoProvider
) {
    suspend fun invoke(codNacional: Long): MedicamentoItem? {
        return miPastilleroDbRepository.findMedicamentoByCodNacional(
            userInfoProvider.currentUser.pkUsuario,
            codNacional
        ) ?: cimaRepository.searchMedicamento(codNacional)
    }
}
