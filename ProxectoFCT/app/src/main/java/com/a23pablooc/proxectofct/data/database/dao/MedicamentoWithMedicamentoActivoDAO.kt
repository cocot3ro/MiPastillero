package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.a23pablooc.proxectofct.data.database.relationships.MedicamentoWithMedicamentoActivo
import kotlinx.coroutines.flow.Flow
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition as M

@Dao
interface MedicamentoWithMedicamentoActivoDAO {

    @Transaction
    @Query(
        """
            SELECT *
            FROM ${M.TABLE_NAME} 
            WHERE ${M.Columns.FK_USUARIO} = :idUsuario
        """
    )
    fun getAll(idUsuario: Long): Flow<List<MedicamentoWithMedicamentoActivo>>
}