package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition

@Entity(
    tableName = HistorialTableDefinition.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTableDefinition.Columns.PK_USUARIO],
            childColumns = [HistorialTableDefinition.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = HistorialTableDefinition.Indexes.IDX_HISTORIAL_FK_USUARIO,
            unique = false,
            value = [HistorialTableDefinition.Columns.FK_USUARIO]
        )
    ]
)
data class HistorialEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = HistorialTableDefinition.Columns.PK_HISTORIAL)
    val pkHistorial: Long = 0,

    @ColumnInfo(name = HistorialTableDefinition.Columns.FK_USUARIO)
    val fkUsuario: Long,

    @Embedded(prefix = HistorialTableDefinition.Prefixes.MEDICAMENTO)
    val fkMedicamento: MedicamentoEntity,

    @Embedded(prefix = HistorialTableDefinition.Prefixes.MEDICAMENTO_ACTIVO)
    val fkMedicamentoActivo: MedicamentoActivoEntity
)