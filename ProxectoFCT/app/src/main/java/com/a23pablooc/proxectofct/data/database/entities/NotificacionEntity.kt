package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition.Columns.FECHA
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition.Columns.FK_MEDICAMENTO_ACTIVO
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition.Columns.FK_USUARIO
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition.Columns.HORA
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition.Columns.PK_NOTIFICACION
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition.Indexes.IDX_NOTIFICACION_FK_MEDICAMENTO
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition.Indexes.IDX_NOTIFICACION_FK_USUARIO
import com.a23pablooc.proxectofct.data.database.definitions.NotificacionTableDefinition.TABLE_NAME
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition
import java.util.Date

@Entity(
    tableName = TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoActivoEntity::class,
            parentColumns = [MedicamentoActivoTableDefinition.Columns.PK_MEDICAMENTO_ACTIVO],
            childColumns = [FK_MEDICAMENTO_ACTIVO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
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
            name = IDX_NOTIFICACION_FK_MEDICAMENTO,
            unique = false,
            value = [FK_MEDICAMENTO_ACTIVO]
        ),
        Index(
            name = IDX_NOTIFICACION_FK_USUARIO,
            unique = false,
            value = [FK_USUARIO]
        )
    ]
)
data class NotificacionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PK_NOTIFICACION)
    val pkNotificacion: Long = 0,

    @ColumnInfo(name = FK_MEDICAMENTO_ACTIVO)
    val fkMedicamentoActivo: Long,

    @ColumnInfo(name = FK_USUARIO)
    val fkUsuario: Long,

    @ColumnInfo(name = FECHA)
    val fecha: Date,

    @ColumnInfo(name = HORA)
    val hora: Date
)