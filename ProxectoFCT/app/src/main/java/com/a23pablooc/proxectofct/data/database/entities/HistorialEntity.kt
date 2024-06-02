package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition
import java.util.Date

@Entity(
    tableName = HistorialTableDefinition.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTableDefinition.Columns.PK_USUARIO],
            childColumns = [HistorialTableDefinition.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO],
            childColumns = [HistorialTableDefinition.Columns.FK_MEDICAMENTO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = HistorialTableDefinition.Indexes.IDX_HISTORIAL_FK_USUARIO,
            unique = false,
            value = [HistorialTableDefinition.Columns.FK_USUARIO]
        ),
        Index(
            name = HistorialTableDefinition.Indexes.IDX_HISTORIAL_FK_MEDICAMENTO,
            unique = false,
            value = [HistorialTableDefinition.Columns.FK_MEDICAMENTO]
        )
    ]
)
data class HistorialEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = HistorialTableDefinition.Columns.PK_HISTORIAL)
    val pkHistorial: Long = 0,

    @ColumnInfo(name = HistorialTableDefinition.Columns.FK_USUARIO)
    val fkUsuario: Long,

    @ColumnInfo(name = HistorialTableDefinition.Columns.FK_MEDICAMENTO)
    val fkMedicamento: Long,

    @ColumnInfo(name = HistorialTableDefinition.Columns.FECHA_INICIO)
    val fechaInicio: Date,

    @ColumnInfo(name = HistorialTableDefinition.Columns.FECHA_FIN)
    val fechaFin: Date
)