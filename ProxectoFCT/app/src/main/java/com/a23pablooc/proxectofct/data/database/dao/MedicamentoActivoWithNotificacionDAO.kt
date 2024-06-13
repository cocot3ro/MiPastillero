package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTableDefinition as Ma
import com.a23pablooc.proxectofct.data.database.relationships.MedicamentoActivoWithNotificacion as M

@Dao
interface MedicamentoActivoWithNotificacionDAO {

    @Transaction
    @Query(
        """
            SELECT *
            FROM ${Ma.TABLE_NAME}
            WHERE ${Ma.Columns.FK_USUARIO} = :idUsuario AND
            ${Ma.Columns.FECHA_INICIO} >= :fromDate
        """
    )
    fun getAllFromDate(
        idUsuario: Int,
        fromDate: Long
    ): Flow<List<M>>
}