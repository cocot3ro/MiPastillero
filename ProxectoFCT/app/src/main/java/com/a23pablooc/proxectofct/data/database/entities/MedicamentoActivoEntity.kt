package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "medicamentos_activos")
data class MedicamentoActivoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PK_medicamento_activo")
    val id: Int = 0,

    @ColumnInfo(name = "fecha_inicio")
    val fechaInicio: Date,

    @ColumnInfo(name = "fecha_fin")
    val fechaFin: Date,

    @ColumnInfo(name = "horario")
    val horario: Set<Date>,

    @ColumnInfo(name = "dosis")
    val dosis: String
)