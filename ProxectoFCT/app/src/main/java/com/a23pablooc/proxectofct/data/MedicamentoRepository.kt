package com.a23pablooc.proxectofct.data

import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import javax.inject.Inject

class MedicamentoRepository @Inject constructor(
    private val medicamentoDAO: MedicamentoDAO
) {
    suspend fun getAll() = medicamentoDAO.getAll()

    /*
    Insert all,
    get from api...
     */
}