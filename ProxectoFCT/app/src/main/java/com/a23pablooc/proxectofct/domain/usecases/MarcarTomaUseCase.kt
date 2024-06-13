package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import java.util.Date
import javax.inject.Inject

class MarcarTomaUseCase @Inject constructor(private val repository: PillboxDbRepository) {

    suspend fun invoke(med: MedicamentoActivoItem, dia: Date, hora: Date, value: Boolean? = null) {
        val today = DateTimeUtils.today.zeroTime()

        if (dia.zeroTime() != today)
            throw IllegalArgumentException("Not today son!")

        if (value == null) {
            med.tomas[dia]?.let { dayMap ->
                dayMap[hora] = dayMap[hora]?.not() ?: true
            } ?: run {
                med.tomas[dia] = mutableMapOf(hora to true)
            }
        } else {
            med.tomas[dia]?.let { dayMap ->
                dayMap[hora] = value
            } ?: run {
                med.tomas[dia] = mutableMapOf(hora to value)
            }
        }

        repository.updateMed(med)
    }
}