package com.cocot3ro.mipastillero.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cocot3ro.mipastillero.data.database.definitions.UsuarioTableDefinition.TABLE_NAME
import com.cocot3ro.mipastillero.data.database.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDAO {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllFlow(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAll(): List<UsuarioEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usuario: UsuarioEntity): Long

    @Update
    suspend fun update(usuario: UsuarioEntity)

    @Delete
    suspend fun delete(usuario: UsuarioEntity)
}