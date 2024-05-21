package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTable
import com.a23pablooc.proxectofct.data.database.entities.AgendaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgendaDAO {

    @Query("SELECT * FROM ${AgendaTable.TABLE_NAME} WHERE ${AgendaTable.Columns.FK_USUARIO} = :idUsuario")
    fun getAll(idUsuario: Int): Flow<List<AgendaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(agenda: AgendaEntity)

    @Update
    suspend fun update(agenda: AgendaEntity)

    @Delete
    suspend fun delete(agenda: AgendaEntity)
}