package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTableDefinition.Columns.FK_USUARIO
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTableDefinition.TABLE_NAME
import com.a23pablooc.proxectofct.data.database.entities.AgendaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgendaDAO {

    @Query("SELECT * FROM $TABLE_NAME WHERE $FK_USUARIO = :idUsuario")
    fun getAll(idUsuario: Int): Flow<List<AgendaEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(agenda: AgendaEntity)

    @Update
    suspend fun update(agenda: AgendaEntity)

    @Delete
    suspend fun delete(agenda: AgendaEntity)
}