package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTable
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoAndMedicamentoActivo
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoAndMedicamentoActivoDAO {

    @Transaction
    @Query(
        """
            SELECT *
            FROM ${MedicamentoActivoTable.TABLE_NAME}
            WHERE ${MedicamentoActivoTable.Columns.FK_USUARIO} = :idUsuario AND
            ${MedicamentoActivoTable.Columns.FECHA_INICIO} >= :fromDate
        """
    )
    fun getAllWithMedicamento(
        idUsuario: Int,
        fromDate: Long
    ): Flow<List<MedicamentoAndMedicamentoActivo>>

    @Transaction
    @Query(
        """
            SELECT *
            FROM ${MedicamentoActivoTable.TABLE_NAME}
            WHERE ${MedicamentoActivoTable.Columns.FK_USUARIO} = :idUsuario AND
                ${MedicamentoActivoTable.Columns.FECHA_INICIO} >= :dia AND
                ${MedicamentoActivoTable.Columns.FECHA_FIN} <= :dia
            ORDER BY ${MedicamentoActivoTable.Columns.HORARIO} ASC
        """
    )
    fun getAllWithMedicamentosByDiaOrderByHora(
        idUsuario: Int,
        dia: Long
    ): Flow<List<MedicamentoAndMedicamentoActivo>>
}