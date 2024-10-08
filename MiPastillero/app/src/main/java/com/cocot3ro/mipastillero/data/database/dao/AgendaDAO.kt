package com.cocot3ro.mipastillero.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.cocot3ro.mipastillero.data.database.definitions.AgendaTableDefinition.Columns.FK_USUARIO
import com.cocot3ro.mipastillero.data.database.definitions.AgendaTableDefinition.Columns.PK_FECHA
import com.cocot3ro.mipastillero.data.database.definitions.AgendaTableDefinition.TABLE_NAME
import com.cocot3ro.mipastillero.data.database.entities.AgendaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgendaDAO {
    @Query("SELECT * FROM $TABLE_NAME WHERE $FK_USUARIO = :idUsuario")
    fun getAll(idUsuario: Long): Flow<List<AgendaEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE $FK_USUARIO = :idUsuario AND $PK_FECHA = :date")
    fun getByFecha(idUsuario: Long, date: Long): Flow<List<AgendaEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(agenda: AgendaEntity)

    @Update
    suspend fun update(agenda: AgendaEntity)

    @Upsert
    suspend fun upsert(agenda: AgendaEntity)

    @Delete
    suspend fun delete(agenda: AgendaEntity)
}