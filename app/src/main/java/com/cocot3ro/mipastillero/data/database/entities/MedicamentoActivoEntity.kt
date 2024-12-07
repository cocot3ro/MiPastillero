package com.cocot3ro.mipastillero.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.Columns.DOSIS
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.Columns.FECHA_FIN
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.Columns.FECHA_INICIO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.Columns.FK_MEDICAMENTO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.Columns.FK_USUARIO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.Columns.HORARIO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.Columns.PK_MEDICAMENTO_ACTIVO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.Columns.TOMAS
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.Indexes.IDX_MEDICAMENTO_ACTIVO_FK_MEDICAMENTO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.Indexes.IDX_MEDICAMENTO_ACTIVO_FK_USUARIO
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoActivoTableDefinition.TABLE_NAME
import com.cocot3ro.mipastillero.data.database.definitions.MedicamentoTableDefinition
import com.cocot3ro.mipastillero.data.database.definitions.UsuarioTableDefinition
import java.util.Date

@Entity(
    tableName = TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MedicamentoEntity::class,
            parentColumns = [MedicamentoTableDefinition.Columns.PK_COD_NACIONAL_MEDICAMENTO],
            childColumns = [FK_MEDICAMENTO],
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
            name = IDX_MEDICAMENTO_ACTIVO_FK_MEDICAMENTO,
            unique = false,
            value = [FK_MEDICAMENTO]
        ),
        Index(
            name = IDX_MEDICAMENTO_ACTIVO_FK_USUARIO,
            unique = false,
            value = [FK_USUARIO]
        )
    ]
)
data class MedicamentoActivoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PK_MEDICAMENTO_ACTIVO)
    var pkMedicamentoActivo: Long = 0,

    @ColumnInfo(name = FK_MEDICAMENTO)
    var fkMedicamento: Long,

    @ColumnInfo(name = FK_USUARIO)
    var fkUsuario: Long,

    @ColumnInfo(name = FECHA_INICIO)
    var fechaInicio: Date,

    @ColumnInfo(name = FECHA_FIN)
    var fechaFin: Date,

    @ColumnInfo(name = HORARIO)
    var horario: MutableSet<Date>,

    @ColumnInfo(name = DOSIS)
    var dosis: String,

    @ColumnInfo(name = TOMAS)
    var tomas: MutableMap<Date, Boolean>
)