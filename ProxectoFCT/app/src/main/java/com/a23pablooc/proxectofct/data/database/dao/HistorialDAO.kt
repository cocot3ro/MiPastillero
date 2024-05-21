package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTable
import com.a23pablooc.proxectofct.data.database.entities.HistorialAndMedicamento
import com.a23pablooc.proxectofct.data.database.entities.HistorialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorialDAO {

    @Query("SELECT * FROM ${HistorialTable.TABLE_NAME} WHERE ${HistorialTable.Columns.FK_USUARIO} = :idUsuario")
    fun getAll(idUsuario: Int): Flow<List<HistorialEntity>>

    @Transaction
    @Query("SELECT * FROM ${HistorialTable.TABLE_NAME} WHERE ${HistorialTable.Columns.FK_USUARIO} = :idUsuario")
    fun getAllWithMedicamentos(idUsuario: Int): Flow<List<HistorialAndMedicamento>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historial: HistorialEntity)

    @Update
    suspend fun update(historial: HistorialEntity)

    @Delete
    suspend fun delete(historial: HistorialEntity)
}