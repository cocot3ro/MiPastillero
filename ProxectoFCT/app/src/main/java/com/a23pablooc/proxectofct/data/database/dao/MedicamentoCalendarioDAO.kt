package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoCalendarioTable
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoCalendarioAndMedicamento
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoCalendarioEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MedicamentoCalendarioDAO {

    @Query("SELECT * FROM ${MedicamentoCalendarioTable.TABLE_NAME} WHERE ${MedicamentoCalendarioTable.Columns.FK_USUARIO} = :idUsuario")
    fun getAll(idUsuario: Int): Flow<List<MedicamentoCalendarioEntity>>

    @Transaction
    @Query("SELECT * FROM ${MedicamentoCalendarioTable.TABLE_NAME} WHERE ${MedicamentoCalendarioTable.Columns.FK_USUARIO} = :idUsuario")
    fun getAllWithMedicamentos(idUsuario: Int): Flow<List<MedicamentoCalendarioAndMedicamento>>

    @Transaction
    @Query(
        """
            SELECT *
            FROM ${MedicamentoCalendarioTable.TABLE_NAME}
            WHERE ${MedicamentoCalendarioTable.Columns.FK_USUARIO} = :idUsuario AND
                ${MedicamentoCalendarioTable.Columns.FECHA} = :dia
            ORDER BY ${MedicamentoCalendarioTable.Columns.HORA} ASC
        """
    )
    fun getAllWithMedicamentosByDiaOrderByHora(
        idUsuario: Int,
        dia: Date
    ): Flow<List<MedicamentoCalendarioAndMedicamento>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calendario: MedicamentoCalendarioEntity)

    @Update
    suspend fun update(calendario: MedicamentoCalendarioEntity)

    @Delete
    suspend fun delete(calendario: MedicamentoCalendarioEntity)
}