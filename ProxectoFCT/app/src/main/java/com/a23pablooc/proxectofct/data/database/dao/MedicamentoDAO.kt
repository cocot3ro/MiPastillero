package com.a23pablooc.proxectofct.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity

@Dao
interface MedicamentoDAO {
    @Query("SELECT * FROM medicamentos")
    suspend fun getAll(): LiveData<List<MedicamentoEntity>>
}