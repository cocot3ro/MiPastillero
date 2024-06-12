package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoHistorialTableDefinition
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoHistorialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoHistorialDAO {

    @Query("SELECT * FROM ${MedicamentoHistorialTableDefinition.TABLE_NAME} WHERE ${MedicamentoHistorialTableDefinition.Columns.FK_USUARIO} = :idUsuario")
    fun getAll(idUsuario: Int): Flow<List<MedicamentoHistorialEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(medicamentoHistorial: MedicamentoHistorialEntity)

    @Update
    suspend fun update(medicamentoHistorial: MedicamentoHistorialEntity)

    @Delete
    suspend fun delete(medicamentoHistorial: MedicamentoHistorialEntity)
}