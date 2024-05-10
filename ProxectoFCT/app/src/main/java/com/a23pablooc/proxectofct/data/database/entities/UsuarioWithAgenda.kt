package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.a23pablooc.proxectofct.data.database.definitions.AgendaTable
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable

data class UsuarioWithAgenda(
    @Embedded val usuario: UsuarioEntity,
    @Relation(
        parentColumn = UsuarioTable.Columns.ID,
        entityColumn = AgendaTable.Columns.FK_USUARIO
    )
    val agenda: List<AgendaEntity>,
)

/*
    en el dao marcar query con @Transaction
 */