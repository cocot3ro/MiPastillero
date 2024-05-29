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
            parentColumns = [UsuarioTable.Columns.PK_USUARIO],
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
    @ColumnInfo(name = MedicamentoTable.Columns.PK_COD_NACIONAL)
    val pkMedicamento: Int = 0,

    @ColumnInfo(name = MedicamentoTable.Columns.FK_USUARIO)
    val fkUsuario: Int,

    @ColumnInfo(name = MedicamentoTable.Columns.URL)
    val url: String,

    @ColumnInfo(name = MedicamentoTable.Columns.NOMBRE)
    val nombre: String,

    @ColumnInfo(name = MedicamentoTable.Columns.PROSPECTO)
    val prospecto: String,

    @ColumnInfo(name = MedicamentoTable.Columns.ES_FAVORITO)
    val esFavorito: Boolean,

    @ColumnInfo(name = MedicamentoTable.Columns.LABORATORIO)
    val laboratorio: String,

    @ColumnInfo(name = MedicamentoTable.Columns.NUM_REGISTRO)
    val numRegistro: String,

    @ColumnInfo(name = MedicamentoTable.Columns.API_IMAGEN)
    val apiImagen: ByteArray,

    @ColumnInfo(name = MedicamentoTable.Columns.PRESCRIPCION)
    val prescripcion: String,

    @ColumnInfo(name = MedicamentoTable.Columns.IMAGEN)
    val customImage: ByteArray,

    @ColumnInfo(name = MedicamentoTable.Columns.AFECTA_CONDUCCION)
    val afectaConduccion: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicamentoEntity

        return ((pkMedicamento != other.pkMedicamento)
                || (fkUsuario != other.fkUsuario)
                || (url != other.url)
                || (nombre != other.nombre)
                || (prospecto != other.prospecto)
                || (numRegistro != other.numRegistro)
                || (laboratorio != other.laboratorio)
                || (esFavorito != other.esFavorito))
                || (!apiImagen.contentEquals(other.apiImagen))
                || (prescripcion != other.prescripcion)
                || (!customImage.contentEquals(other.customImage))
                || (afectaConduccion != other.afectaConduccion)
    }

    override fun hashCode(): Int {
        var result = pkMedicamento
        result = 31 * result + fkUsuario
        result = 31 * result + url.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + prospecto.hashCode()
        result = 31 * result + numRegistro.hashCode()
        result = 31 * result + laboratorio.hashCode()
        result = 31 * result + esFavorito.hashCode()
        result = 31 * result + apiImagen.contentHashCode()
        result = 31 * result + prescripcion.hashCode()
        result = 31 * result + customImage.contentHashCode()
        result = 31 * result + afectaConduccion.hashCode()
        return result
    }
}