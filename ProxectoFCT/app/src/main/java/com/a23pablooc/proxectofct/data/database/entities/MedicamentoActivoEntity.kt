package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "active_meds")
data class MedicamentoActivoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PK_medicamento_activo")
    val id: Int = 0,

    @ColumnInfo(name = "fecha_inicio")
    val dateStart: Date,

    @ColumnInfo(name = "fecha_fin")
    val dateEnd: Date,

    @ColumnInfo(name = "horario")
    val schedule: Set<Date>,
)