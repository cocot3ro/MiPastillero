package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTableDefinition
import com.a23pablooc.proxectofct.data.database.entities.HistorialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorialDAO {

    @Query("SELECT * FROM ${HistorialTableDefinition.TABLE_NAME} WHERE ${HistorialTableDefinition.Columns.FK_USUARIO} = :idUsuario")
    fun getAll(idUsuario: Int): Flow<List<HistorialEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(historial: HistorialEntity)

    @Update
    suspend fun update(historial: HistorialEntity)

    @Delete
    suspend fun delete(historial: HistorialEntity)
}