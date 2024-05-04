package com.a23pablooc.proxectofct.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTable

@Dao
interface MedicamentoActivoDAO {

    @Query("SELECT * FROM ${MedicamentoActivoTable.TABLE_NAME} WHERE ${MedicamentoActivoTable.Columns.FK_USUARIO} = :idUsuario")
    fun getAll(idUsuario: Int): LiveData<List<MedicamentoActivoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicamentoActivo: MedicamentoActivoEntity)

    @Update
    suspend fun update(medicamentoActivo: MedicamentoActivoEntity)

    @Delete
    suspend fun delete(medicamentoActivo: MedicamentoActivoEntity)
}