package com.a23pablooc.proxectofct.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a23pablooc.proxectofct.data.database.definitions.MedicamentoTable
import com.a23pablooc.proxectofct.data.database.definitions.UsuarioTable

@Entity(
    tableName = MedicamentoTable.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = [UsuarioTable.Columns.ID],
            childColumns = [MedicamentoTable.Columns.FK_USUARIO],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = [MedicamentoTable.Columns.FK_USUARIO],
            name = MedicamentoTable.Indexes.IDX_MEDICAMENTO_USER_ID,
            unique = false
        )
    ]
)
data class MedicamentoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MedicamentoTable.Columns.ID)
    val id: Int = 0,

    @ColumnInfo(name = MedicamentoTable.Columns.FK_USUARIO)
    val userId: Int,

    @ColumnInfo(name = MedicamentoTable.Columns.NUM_REGISTRO)
    val numRegistro: String,

    @ColumnInfo(name = MedicamentoTable.Columns.NOMBRE)
    val nombre: String,

    @ColumnInfo(name = MedicamentoTable.Columns.URL)
    val url: String,

    @ColumnInfo(name = MedicamentoTable.Columns.PROSPECTO)
    val prospecto: String,

    @ColumnInfo(name = MedicamentoTable.Columns.IMAGEN)
    val imagen: ByteArray,

    @ColumnInfo(name = MedicamentoTable.Columns.LABORATORIO)
    val laboratorio: String,

    @ColumnInfo(name = MedicamentoTable.Columns.PRESCRIPCION)
    val prescripcion: String,

    @ColumnInfo(name = MedicamentoTable.Columns.AFECTA_CONDUCCION)
    val afectaConduccion: Boolean,

    @ColumnInfo(name = MedicamentoTable.Columns.ES_FAVORITO)
    val esFavorito: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoEntity

        if (id != other.id) return false
        if (numRegistro != other.numRegistro) return false
        if (nombre != other.nombre) return false
        if (url != other.url) return false
        if (prospecto != other.prospecto) return false
        if (!imagen.contentEquals(other.imagen)) return false
        if (laboratorio != other.laboratorio) return false
        if (prescripcion != other.prescripcion) return false
        if (afectaConduccion != other.afectaConduccion) return false
        if (esFavorito != other.esFavorito) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + numRegistro.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + prospecto.hashCode()
        result = 31 * result + imagen.contentHashCode()
        result = 31 * result + laboratorio.hashCode()
        result = 31 * result + prescripcion.hashCode()
        result = 31 * result + afectaConduccion.hashCode()
        result = 31 * result + esFavorito.hashCode()
        return result
    }
}