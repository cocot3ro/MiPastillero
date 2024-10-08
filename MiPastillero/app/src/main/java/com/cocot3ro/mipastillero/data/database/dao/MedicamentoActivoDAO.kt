package com.cocot3ro.mipastillero.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.Columns.FK_USUARIO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.TABLE_NAME
import com.cocot3ro.mipastillero.data.database.entities.MedicamentoActivoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoActivoDAO {

    @Query("SELECT * FROM $TABLE_NAME WHERE $FK_USUARIO = :idUsuario")
    fun getAll(idUsuario: Int): Flow<List<MedicamentoActivoEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(medicamentoActivo: MedicamentoActivoEntity)

    @Update
    suspend fun update(medicamentoActivo: MedicamentoActivoEntity)

    @Delete
    suspend fun delete(medicamentoActivo: MedicamentoActivoEntity)
}