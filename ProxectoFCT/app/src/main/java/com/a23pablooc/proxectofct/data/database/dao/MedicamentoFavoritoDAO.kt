package com.a23pablooc.proxectofct.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoFavoritoTable
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoFavoritoAndMedicamento
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoFavoritoEntity

@Dao
interface MedicamentoFavoritoDAO {

    @Query("SELECT * FROM ${MedicamentoFavoritoTable.TABLE_NAME} WHERE ${MedicamentoFavoritoTable.Columns.FK_USUARIO} = :idUsuario")
    fun getAll(idUsuario: Int): LiveData<List<MedicamentoFavoritoEntity>>

    @Transaction
    @Query("SELECT * FROM ${MedicamentoFavoritoTable.TABLE_NAME} WHERE ${MedicamentoFavoritoTable.Columns.FK_USUARIO} = :idUsuario")
    fun getAllWithMedicamentos(idUsuario: Int): LiveData<List<MedicamentoFavoritoAndMedicamento>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicamentoFavorito: MedicamentoFavoritoEntity)

    @Update
    suspend fun update(medicamentoFavorito: MedicamentoFavoritoEntity)

    @Delete
    suspend fun delete(medicamentoFavorito: MedicamentoFavoritoEntity)
}