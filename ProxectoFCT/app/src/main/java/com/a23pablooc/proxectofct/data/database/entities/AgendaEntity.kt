package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTableDefinition.Columns.FECHA
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTableDefinition.Columns.FK_USUARIO
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTableDefinition.Columns.HORA
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTableDefinition.Columns.PK_AGENDA
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTableDefinition.Indexes.IDX_AGENDA_FK_USUARIO
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTableDefinition.TABLE_NAME
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition
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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PK_AGENDA)
    val pkAgenda: Long = 0,

    @ColumnInfo(name = FK_USUARIO)
    val fkUsuario: Long,

    @ColumnInfo(name = HORA)
    val descripcion: String,

    @ColumnInfo(name = FECHA)
    val fecha: Date
)