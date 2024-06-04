package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTableDefinition
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoActivoDAO {

    @Query("SELECT * FROM ${MedicamentoActivoTableDefinition.TABLE_NAME} WHERE ${MedicamentoActivoTableDefinition.Columns.FK_USUARIO} = :idUsuario")
    fun getAll(idUsuario: Int): Flow<List<MedicamentoActivoEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(medicamentoActivo: MedicamentoActivoEntity)

    @Update
    suspend fun update(medicamentoActivo: MedicamentoActivoEntity)

    @Delete
    suspend fun delete(medicamentoActivo: MedicamentoActivoEntity)

    @Delete
    suspend fun deleteAll(medicamentoActivo: List<MedicamentoActivoEntity>)
}