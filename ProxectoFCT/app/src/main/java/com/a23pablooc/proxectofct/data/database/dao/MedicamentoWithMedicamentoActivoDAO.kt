package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoWithMedicamentoActivo
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoWithMedicamentoActivoDAO {

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
            SELECT *
            FROM ${MedicamentoTableDefinition.TABLE_NAME} AS m
            INNER JOIN ${MedicamentoActivoTableDefinition.TABLE_NAME} AS ma
                ON m.${MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO} = ma.${MedicamentoActivoTableDefinition.Columns.FK_MEDICAMENTO}
            WHERE m.${MedicamentoTableDefinition.Columns.FK_USUARIO} = :idUsuario
                AND (ma.${MedicamentoActivoTableDefinition.Columns.FECHA_INICIO} <= :fromDate AND :fromDate <= ma.${MedicamentoActivoTableDefinition.Columns.FECHA_FIN}
                OR ma.${MedicamentoActivoTableDefinition.Columns.FECHA_INICIO} >= :fromDate)
        """
    )
    fun getAllFromDate(
        idUsuario: Long,
        fromDate: Long
    ): Flow<List<MedicamentoWithMedicamentoActivo>>

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
            SELECT *
            FROM ${MedicamentoTableDefinition.TABLE_NAME} AS m
            INNER JOIN ${MedicamentoActivoTableDefinition.TABLE_NAME} AS ma
                ON m.${MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO} = ma.${MedicamentoActivoTableDefinition.Columns.FK_MEDICAMENTO}
            WHERE ma.${MedicamentoActivoTableDefinition.Columns.FK_USUARIO} = :idUsuario
                AND ma.${MedicamentoActivoTableDefinition.Columns.FECHA_INICIO} <= :dia
                AND ma.${MedicamentoActivoTableDefinition.Columns.FECHA_FIN} >= :dia
            ORDER BY ma.${MedicamentoActivoTableDefinition.Columns.HORARIO} ASC
        """
    )
    fun getAllByDiaOrderByHora(
        idUsuario: Long,
        dia: Long
    ): Flow<List<MedicamentoWithMedicamentoActivo>>

}