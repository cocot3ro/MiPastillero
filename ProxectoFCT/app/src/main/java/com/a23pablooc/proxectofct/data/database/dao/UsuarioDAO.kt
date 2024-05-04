package com.a23pablooc.proxectofct.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.entities.UsuarioEntity
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable

@Dao
interface UsuarioDAO {
    @Query("SELECT * FROM ${UsuarioTable.TABLE_NAME}")
    fun getAll(): LiveData<List<UsuarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: UsuarioEntity)

    @Update
    suspend fun update(usuario: UsuarioEntity)

    @Delete
    suspend fun delete(usuario: UsuarioEntity)
}