package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.Date
//TODO: archivo de constantes AgendaTable.kt
data class AgendaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Date,

    @ColumnInfo(name = "descripcion")
    val descripcion: String
)
