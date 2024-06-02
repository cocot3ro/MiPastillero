package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoActivoTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTableDefinition
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTableDefinition
import java.util.Date

@Entity(
    tableName = MedicamentoActivoTableDefinition.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO],
            childColumns = [MedicamentoActivoTableDefinition.Columns.FK_MEDICAMENTO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTableDefinition.Columns.PK_USUARIO],
            childColumns = [MedicamentoActivoTableDefinition.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            name = MedicamentoActivoTableDefinition.Indexes.IDX_MEDICAMENTO_ACTIVO_FK_MEDICAMENTO,
            unique = false,
            value = [MedicamentoActivoTableDefinition.Columns.FK_MEDICAMENTO]
        ),
        Index(
            name = MedicamentoActivoTableDefinition.Indexes.IDX_MEDICAMENTO_ACTIVO_FK_USUARIO,
            unique = false,
            value = [MedicamentoActivoTableDefinition.Columns.FK_USUARIO]
        )
    ]
)
data class MedicamentoActivoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MedicamentoActivoTableDefinition.Columns.PK_MEDICAMENTO_ACTIVO)
    var pkMedicamentoActivo: Long = 0,

    @ColumnInfo(name = MedicamentoActivoTableDefinition.Columns.FK_MEDICAMENTO)
    var fkMedicamento: Long,

    @ColumnInfo(name = MedicamentoActivoTableDefinition.Columns.FK_USUARIO)
    var fkUsuario: Long,

    @ColumnInfo(name = MedicamentoActivoTableDefinition.Columns.FECHA_INICIO)
    var fechaInicio: Date,

    @ColumnInfo(name = MedicamentoActivoTableDefinition.Columns.FECHA_FIN)
    var fechaFin: Date,

    @ColumnInfo(name = MedicamentoActivoTableDefinition.Columns.HORARIO)
    var horario: MutableSet<Date>,

    @ColumnInfo(name = MedicamentoActivoTableDefinition.Columns.DOSIS)
    var dosis: String,

    @ColumnInfo(name = MedicamentoActivoTableDefinition.Columns.TOMAS)
    var tomas: MutableMap<Date, MutableMap<Date, Boolean>>
)