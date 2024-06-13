package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition.Columns.FK_USUARIO
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition.TABLE_NAME
import com.a23pablooc.proxectofct.data.database.entities.NotificacionEntity

@Dao
interface NotificacionDAO {

    @Query("SELECT * FROM $TABLE_NAME WHERE $FK_USUARIO = :idUsuario")
    suspend fun getAll(idUsuario: Int): List<NotificacionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notificacion: NotificacionEntity)

    @Update
    suspend fun update(notificacion: NotificacionEntity)

    @Delete
    suspend fun delete(notificacion: NotificacionEntity)
}