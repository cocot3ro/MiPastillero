package com.a23pablooc.proxectofct.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoCalendarioEntity
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoCalendarioTable

@Dao
interface CalendarioDAO {

    @Query("SELECT * FROM ${MedicamentoCalendarioTable.TABLE_NAME} WHERE ${MedicamentoCalendarioTable.Columns.FK_USUARIO} = :idUsuario")
    fun getAll(idUsuario: Int): LiveData<List<MedicamentoCalendarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calendario: MedicamentoCalendarioEntity)

    @Update
    suspend fun update(calendario: MedicamentoCalendarioEntity)

    @Delete
    suspend fun delete(calendario: MedicamentoCalendarioEntity)
}