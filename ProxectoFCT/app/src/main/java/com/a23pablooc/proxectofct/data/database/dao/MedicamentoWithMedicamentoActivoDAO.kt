package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition
import com.a23pablooc.proxectofct.data.database.relationships.MedicamentoWithMedicamentoActivo
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoWithMedicamentoActivoDAO {

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
            SELECT *
            FROM ${MedicamentoTableDefinition.TABLE_NAME}
            INNER JOIN ${MedicamentoActivoTableDefinition.TABLE_NAME}
                ON ${MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO} = ${MedicamentoActivoTableDefinition.Columns.FK_MEDICAMENTO} 
            WHERE ${MedicamentoTableDefinition.Columns.FK_USUARIO} = :idUsuario
                AND ${MedicamentoActivoTableDefinition.Columns.FECHA_INICIO} >= :date
        """
    )
    fun getAllFromDate(idUsuario: Long, date: Long): Flow<List<MedicamentoWithMedicamentoActivo>>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
            SELECT *
            FROM ${MedicamentoTableDefinition.TABLE_NAME}
            INNER JOIN ${MedicamentoActivoTableDefinition.TABLE_NAME}
                ON ${MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO} = ${MedicamentoActivoTableDefinition.Columns.FK_MEDICAMENTO}
            WHERE ${MedicamentoActivoTableDefinition.Columns.FK_USUARIO} = :idUsuario
                AND ${MedicamentoActivoTableDefinition.Columns.FECHA_INICIO} <= :dia
                AND ${MedicamentoActivoTableDefinition.Columns.FECHA_FIN} >= :dia
            ORDER BY ${MedicamentoActivoTableDefinition.Columns.HORARIO} ASC
        """
    )
    fun getAllByDiaOrderByHora(
        idUsuario: Long,
        dia: Long
    ): Flow<List<MedicamentoWithMedicamentoActivo>>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
            SELECT *
            FROM ${MedicamentoTableDefinition.TABLE_NAME}
            INNER JOIN ${MedicamentoActivoTableDefinition.TABLE_NAME}
                ON ${MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO} = ${MedicamentoActivoTableDefinition.Columns.FK_MEDICAMENTO}
            WHERE ${MedicamentoActivoTableDefinition.Columns.FK_USUARIO} = :idUsuario
                AND ${MedicamentoActivoTableDefinition.Columns.FECHA_FIN} < :date
        """
    )
    fun getMedicamentosTerminados(
        idUsuario: Long,
        date: Long
    ): List<MedicamentoWithMedicamentoActivo>
}