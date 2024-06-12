package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoHistorialTableDefinition
import com.a23pablooc.proxectofct.data.database.relationships.MedicamentoHistorialAndMedicamento
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoHistorialAndMedicamentoActivoDAO {

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
            SELECT *
            FROM ${MedicamentoHistorialTableDefinition.TABLE_NAME}
            WHERE ${MedicamentoHistorialTableDefinition.Columns.FK_USUARIO} = :idUsuario
        """
    )
    fun getAllFromDate(idUsuario: Long): Flow<List<MedicamentoHistorialAndMedicamento>>
}