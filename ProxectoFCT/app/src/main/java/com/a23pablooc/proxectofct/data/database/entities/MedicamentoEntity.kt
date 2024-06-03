package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition

@Entity(
    tableName = MedicamentoTableDefinition.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTableDefinition.Columns.PK_USUARIO],
            childColumns = [MedicamentoTableDefinition.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = [MedicamentoTableDefinition.Columns.FK_USUARIO],
            name = MedicamentoTableDefinition.Indexes.IDX_MEDICAMENTO_USER_ID,
            unique = false
        )
    ]
)
data class MedicamentoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO)
    var pkCodNacionalMedicamento: Long = 0,

    @ColumnInfo(name = MedicamentoTableDefinition.Columns.FK_USUARIO)
    var fkUsuario: Long,

    @ColumnInfo(name = MedicamentoTableDefinition.Columns.URL)
    var url: String,

    @ColumnInfo(name = MedicamentoTableDefinition.Columns.NOMBRE)
    var nombre: String,

    @ColumnInfo(name = MedicamentoTableDefinition.Columns.ALIAS)
    var alias: String,

    @ColumnInfo(name = MedicamentoTableDefinition.Columns.PROSPECTO)
    var prospecto: String,

    @ColumnInfo(name = MedicamentoTableDefinition.Columns.ES_FAVORITO)
    var esFavorito: Boolean,

    @ColumnInfo(name = MedicamentoTableDefinition.Columns.LABORATORIO)
    var laboratorio: String,

    @ColumnInfo(name = MedicamentoTableDefinition.Columns.NUM_REGISTRO)
    var numRegistro: String,

    @ColumnInfo(name = MedicamentoTableDefinition.Columns.IMAGEN)
    var imagen: String,

    @ColumnInfo(name = MedicamentoTableDefinition.Columns.PRESCRIPCION)
    var prescripcion: String,

    @ColumnInfo(name = MedicamentoTableDefinition.Columns.AFECTA_CONDUCCION)
    var afectaConduccion: Boolean
)