package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTableDefinition
import com.a23pablooc.proxectofct.data.database.relationships.MedicamentoActivoWithNotificacion
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoActivoWithNotificacionDAO {

    @Transaction
    @Query(
        """
            SELECT *
            FROM ${MedicamentoActivoTableDefinition.TABLE_NAME}
            WHERE ${MedicamentoActivoTableDefinition.Columns.FK_USUARIO} = :idUsuario AND
            ${MedicamentoActivoTableDefinition.Columns.FECHA_INICIO} >= :fromDate
        """
    )
    fun getAllFromDate(
        idUsuario: Int,
        fromDate: Long
    ): Flow<List<MedicamentoActivoWithNotificacion>>
}