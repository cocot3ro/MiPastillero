package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTable
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoAndMedicamento
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MedicamentoActivoDAO {

    @Query("SELECT * FROM ${MedicamentoActivoTable.TABLE_NAME} WHERE ${MedicamentoActivoTable.Columns.FK_USUARIO} = :idUsuario")
    fun getAll(idUsuario: Int): Flow<List<MedicamentoActivoEntity>>

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
        fromDate: Date
    ): Flow<List<MedicamentoActivoAndMedicamento>>

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
        dia: Date
    ): Flow<List<MedicamentoActivoAndMedicamento>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicamentoActivo: MedicamentoActivoEntity)

    @Update
    suspend fun update(medicamentoActivo: MedicamentoActivoEntity)

    @Delete
    suspend fun delete(medicamentoActivo: MedicamentoActivoEntity)
}