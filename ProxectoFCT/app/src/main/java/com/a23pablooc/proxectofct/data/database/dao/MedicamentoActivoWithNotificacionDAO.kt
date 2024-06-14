package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.a23pablooc.proxectofct.data.database.relationships.MedicamentoActivoWithNotificacion
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTableDefinition as Ma
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition as N

@Dao
interface MedicamentoActivoWithNotificacionDAO {

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
            SELECT *
            FROM ${Ma.TABLE_NAME}
            INNER JOIN ${N.TABLE_NAME}
                ON ${N.Columns.FK_MEDICAMENTO_ACTIVO} = ${Ma.Columns.PK_MEDICAMENTO_ACTIVO} 
            WHERE ${Ma.Columns.FK_USUARIO} = :idUsuario
                AND ${N.Columns.FK_MEDICAMENTO_ACTIVO} = :medActivo
        """
    )
    suspend fun getAll(idUsuario: Long, medActivo: Long): List<MedicamentoActivoWithNotificacion>
}