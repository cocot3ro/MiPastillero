package com.a23pablooc.proxectofct.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoDAO {
    @Query(
        """
            SELECT *
            FROM ${MedicamentoTableDefinition.TABLE_NAME}
        """
    )
    fun getAll(): Flow<List<MedicamentoEntity>>

    @Query(
        """
            SELECT *
            FROM ${MedicamentoTableDefinition.TABLE_NAME}
            WHERE ${MedicamentoTableDefinition.Columns.FK_USUARIO} = :idUsuario
                AND ${MedicamentoTableDefinition.Columns.ES_FAVORITO} = 1
        """
    )
    fun getAllFavoritos(idUsuario: Long): Flow<List<MedicamentoEntity>>

    @Query(
        """
            SELECT *
            FROM ${MedicamentoTableDefinition.TABLE_NAME}
            WHERE ${MedicamentoTableDefinition.Columns.FK_USUARIO} = :userId
                AND ${MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO} = :codNacional
                AND ${MedicamentoTableDefinition.Columns.ES_FAVORITO} = :favorito
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
            FROM ${MedicamentoTableDefinition.TABLE_NAME}
            WHERE ${MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO} = :codNacional AND
                ${MedicamentoTableDefinition.Columns.FK_USUARIO} = :userId
        """
    )
    fun findByCodNacional(userId: Long, codNacional: Long): MedicamentoEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(medicamento: MedicamentoEntity): Long

    @Upsert
    suspend fun upsert(medicamento: MedicamentoEntity): Long?

    @Update
    suspend fun update(medicamento: MedicamentoEntity)

    @Delete
    suspend fun delete(medicamento: MedicamentoEntity)
}