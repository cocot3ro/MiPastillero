package com.a23pablooc.proxectofct.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity

@Dao
interface MedicamentoDAO {
    @Query("SELECT * FROM medicamentos")
    fun getAll(): LiveData<List<MedicamentoEntity>>

    @Query("SELECT * FROM medicamentos WHERE PK_medicamento = :id")
    fun getById(id: Int): LiveData<MedicamentoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicamento: MedicamentoEntity)

    @Update
    suspend fun update(medicamento: MedicamentoEntity)

    @Delete
    suspend fun delete(medicamento: MedicamentoEntity)
}