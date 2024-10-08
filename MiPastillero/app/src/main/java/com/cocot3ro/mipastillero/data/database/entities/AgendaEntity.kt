package com.cocot3ro.mipastillero.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cocot3ro.mipastillero.data.database.definitions.AgendaTableDefinition.Columns.FK_USUARIO
import com.cocot3ro.mipastillero.data.database.definitions.AgendaTableDefinition.Columns.DESCRIPCION
import com.cocot3ro.mipastillero.data.database.definitions.AgendaTableDefinition.Columns.PK_FECHA
import com.cocot3ro.mipastillero.data.database.definitions.AgendaTableDefinition.Indexes.IDX_AGENDA_FK_USUARIO
import com.cocot3ro.mipastillero.data.database.definitions.AgendaTableDefinition.TABLE_NAME
import com.cocot3ro.mipastillero.data.database.definitions.UsuarioTableDefinition
import java.util.Date

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
            name = IDX_AGENDA_FK_USUARIO,
            value = [FK_USUARIO],
            unique = false
        ),
    ]
)
data class AgendaEntity(
    @PrimaryKey
    @ColumnInfo(name = PK_FECHA)
    val pkFecha: Date,

    @ColumnInfo(name = FK_USUARIO)
    val fkUsuario: Long,

    @ColumnInfo(name = DESCRIPCION)
    val descripcion: String
)