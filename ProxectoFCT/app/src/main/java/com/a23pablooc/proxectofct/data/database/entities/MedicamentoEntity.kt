package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable
import lombok.EqualsAndHashCode

@EqualsAndHashCode
@Entity(tableName = MedicamentoTable.TABLE_NAME)
data class MedicamentoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MedicamentoTable.Columns.ID)
    val id: Int = 0,

    @ColumnInfo(name = MedicamentoTable.Columns.NUM_REGISTRO)
    val numRegistro: String,

    @ColumnInfo(name = MedicamentoTable.Columns.NOMBRE)
    val nombre: String,

    @ColumnInfo(name = MedicamentoTable.Columns.URL)
    val url: String,

    @ColumnInfo(name = MedicamentoTable.Columns.PROSPECTO)
    val prospecto: String,

    @ColumnInfo(name = MedicamentoTable.Columns.IMAGEN)
    val imagen: ByteArray,

    @ColumnInfo(name = MedicamentoTable.Columns.LABORATORIO)
    val laboratorio: String,

    @ColumnInfo(name = MedicamentoTable.Columns.PRESCRIPCION)
    val prescripcion: String,

    @ColumnInfo(name = MedicamentoTable.Columns.AFECTA_CONDUCCION)
    val afectaConduccion: Boolean
)