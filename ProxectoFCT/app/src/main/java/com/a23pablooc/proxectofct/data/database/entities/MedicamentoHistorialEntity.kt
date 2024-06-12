package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoHistorialTableDefinition.Columns.FK_MEDICAMENTO_ACTIVO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoHistorialTableDefinition.Columns.FK_USUARIO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoHistorialTableDefinition.Columns.PK_MEDICAMENTO_HISTORIAL
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoHistorialTableDefinition.Indexes.IDX_MEDICAMENTO_HISTORIAL_FK_USUARIO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoHistorialTableDefinition.Prefixes.PFX_MEDICAMENTO
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoHistorialTableDefinition.TABLE_NAME
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
            name = IDX_MEDICAMENTO_HISTORIAL_FK_USUARIO,
            unique = false,
            value = [FK_USUARIO]
        )
    ]
)
data class MedicamentoHistorialEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PK_MEDICAMENTO_HISTORIAL)
    val pkMedicamentoHistorial: Long = 0,

    @ColumnInfo(name = FK_USUARIO)
    val fkUsuario: Long,

    @ColumnInfo(name = FK_MEDICAMENTO_ACTIVO)
    val fkMedicamentoActivo: Long,

    @Embedded(prefix = PFX_MEDICAMENTO)
    val fkMedicamento: MedicamentoEntity
)