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
    @Query(
        """
            SELECT *
            FROM ${MedicamentoTable.TABLE_NAME}
        """
    )
    fun getAll(): Flow<List<MedicamentoEntity>>

    @Query(
        """
            SELECT *
            FROM ${MedicamentoTable.TABLE_NAME}
            WHERE ${MedicamentoTable.Columns.FK_USUARIO} = :idUsuario
                AND ${MedicamentoTable.Columns.ES_FAVORITO} = 1
        """
    )
    fun getAllFavoritos(idUsuario: Int): Flow<List<MedicamentoEntity>>

    @Query(
        """
            SELECT *
            FROM ${MedicamentoTable.TABLE_NAME}
            WHERE ${MedicamentoTable.Columns.FK_USUARIO} = :userId
                AND ${MedicamentoTable.Columns.PK_COD_NACIONAL_MEDICAMENTO} = :codNacional
                AND ${MedicamentoTable.Columns.ES_FAVORITO} = :favorito
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
            FROM ${MedicamentoTable.TABLE_NAME}
            WHERE ${MedicamentoTable.Columns.PK_COD_NACIONAL_MEDICAMENTO} = :codNacional AND
                ${MedicamentoTable.Columns.FK_USUARIO} = :userId
        """
    )
    fun findByCodNacional(userId: Int, codNacional: Int): MedicamentoEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(medicamento: MedicamentoEntity)

    @Update
    suspend fun update(medicamento: MedicamentoEntity)

    @Delete
    suspend fun delete(medicamento: MedicamentoEntity)
}