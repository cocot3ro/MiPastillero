package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "medicamentos_activos",
    foreignKeys = [ForeignKey(
        entity = MedicamentoEntity::class,
        parentColumns = ["PK_medicamento"],
        childColumns = ["FK_med_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class MedicamentoActivoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PK_med_activo")
    val id: Int = 0,

    @ColumnInfo(name = "FK_med_id")
    val medId: Int,

    @ColumnInfo(name = "fecha_inicio")
    val fechaInicio: Long,

    @ColumnInfo(name = "fecha_fin")
    val fechaFin: Long,

    @ColumnInfo(name = "horario")
    val horario: Set<Long>,
)
