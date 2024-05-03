package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.table_definitions.AgendaTable
import java.util.Date

@Entity(tableName = AgendaTable.TABLE_NAME)
data class AgendaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = AgendaTable.Columns.ID)
    val id: Int = 0,

    @ColumnInfo(name = AgendaTable.Columns.FK_USUARIO)
    val idUsuario: Int,

    @ColumnInfo(name = AgendaTable.Columns.FECHA)
    val fecha: Date,

    @ColumnInfo(name = AgendaTable.Columns.HORA)
    val descripcion: String
)
