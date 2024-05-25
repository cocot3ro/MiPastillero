package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoDAO {
    @Query("SELECT * FROM ${MedicamentoTable.TABLE_NAME}")
    fun getAll(): Flow<List<MedicamentoEntity>>

    @Query("SELECT * FROM ${MedicamentoTable.TABLE_NAME} WHERE ${MedicamentoTable.Columns.FK_USUARIO} = :idUsuario AND ${MedicamentoTable.Columns.ES_FAVORITO} = 1")
    fun getAllFavoritos(idUsuario: Int): Flow<List<MedicamentoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicamento: MedicamentoEntity)

    @Update
    suspend fun update(medicamento: MedicamentoEntity)

    @Delete
    suspend fun delete(medicamento: MedicamentoEntity)
}