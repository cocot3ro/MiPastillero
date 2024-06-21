package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.ES_FAVORITO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.FK_USUARIO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition.TABLE_NAME
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoDAO {
    @Query(
        """
            SELECT *
            FROM $TABLE_NAME
        """
    )
    fun getAll(): Flow<List<MedicamentoEntity>>

    @Query(
        """
            SELECT *
            FROM $TABLE_NAME
            WHERE $FK_USUARIO = :idUsuario
                AND $ES_FAVORITO = 1
        """
    )
    fun getAllFavoritos(idUsuario: Long): Flow<List<MedicamentoEntity>>

    @Query(
        """
            SELECT *
            FROM $TABLE_NAME
            WHERE $FK_USUARIO = :userId
                AND $PK_COD_NACIONAL_MEDICAMENTO = :codNacional
                AND $ES_FAVORITO = :favorito
        """
    )
    fun findMedicamentoByCodNacionalWhereFavorito(
        userId: Int,
        codNacional: Int,
        favorito: Boolean
    ): MedicamentoEntity?

    @Query(
        """
            SELECT *
            FROM $TABLE_NAME
            WHERE $PK_COD_NACIONAL_MEDICAMENTO = :codNacional AND
                $FK_USUARIO = :userId
        """
    )
    fun findByCodNacional(userId: Long, codNacional: Long): MedicamentoEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(medicamento: MedicamentoEntity): Long

    @Upsert
    suspend fun upsert(medicamento: MedicamentoEntity): Long

    @Update
    suspend fun update(medicamento: MedicamentoEntity)

    @Delete
    suspend fun delete(medicamento: MedicamentoEntity)

    @Query(
        """
            UPDATE $TABLE_NAME
            SET $PK_COD_NACIONAL_MEDICAMENTO = :newCodNacional
            WHERE $PK_COD_NACIONAL_MEDICAMENTO = :oldCodNacional
        """
    )
    suspend fun updateCodNacional(oldCodNacional: Long, newCodNacional: Long)
}