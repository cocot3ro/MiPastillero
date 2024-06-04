package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTableDefinition.Columns.FK_USUARIO
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTableDefinition.Columns.PK_HISTORIAL
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTableDefinition.Indexes.IDX_HISTORIAL_FK_USUARIO
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTableDefinition.Prefixes.MEDICAMENTO
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTableDefinition.Prefixes.MEDICAMENTO_ACTIVO
import com.a23pablooc.proxectofct.data.database.definitions.HistorialTableDefinition.TABLE_NAME
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition

@Entity(
    tableName = TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTableDefinition.Columns.PK_USUARIO],
            childColumns = [FK_USUARIO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = IDX_HISTORIAL_FK_USUARIO,
            unique = false,
            value = [FK_USUARIO]
        )
    ]
)
data class HistorialEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PK_HISTORIAL)
    val pkHistorial: Long = 0,

    @ColumnInfo(name = FK_USUARIO)
    val fkUsuario: Long,

    @Embedded(prefix = MEDICAMENTO)
    val fkMedicamento: MedicamentoEntity,

    @Embedded(prefix = MEDICAMENTO_ACTIVO)
    val fkMedicamentoActivo: MedicamentoActivoEntity
)