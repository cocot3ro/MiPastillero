package com.a23pablooc.proxectofct.data

import com.a23pablooc.proxectofct.core.CimaImageType
import com.a23pablooc.proxectofct.data.database.dao.AgendaDAO
import com.a23pablooc.proxectofct.data.database.dao.HistorialDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoCalendarioDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import com.a23pablooc.proxectofct.data.database.dao.NotificacionDAO
import com.a23pablooc.proxectofct.data.database.dao.UsuarioDAO
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.data.database.entities.extensions.toDomain
import com.a23pablooc.proxectofct.data.network.CimaService
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoCalendarioItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.model.extensions.toDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class PillboxRepository @Inject constructor(
    private val usuarioDAO: UsuarioDAO,
    private val medicamentoDAO: MedicamentoDAO,
    private val medicamentoActivoDAO: MedicamentoActivoDAO,
    private val medicamentoCalendarioDAO: MedicamentoCalendarioDAO,
    private val agendaDAO: AgendaDAO,
    private val historialDAO: HistorialDAO,
    private val notificacionDAO: NotificacionDAO,
    private val cimaService: CimaService
) {
    /*
    TODO:
        Insert all,
        get from api...
        Insert MedicamentoActivoConMedicamento -> insetar medicamento y luego insertar medicamento activo
     */

    suspend fun downloadImage(
        imageType: CimaImageType,
        nregistro: String,
        imgResource: String
    ): ByteArray? {
        return cimaService.getMedImage(imageType, nregistro, imgResource)
    }

    fun getAllWithMedicamentosByDiaOrderByHora(
        userId: Int,
        dia: Date
    ): Flow<List<MedicamentoCalendarioItem>> =
        medicamentoCalendarioDAO.getAllWithMedicamentosByDiaOrderByHora(userId, dia)
            .map { it -> it.map { it.toDomain() } }

    suspend fun updateMedicamentoCalendario(med: MedicamentoCalendarioItem) {
        medicamentoCalendarioDAO.update(med.toDatabase().medicamentoCalendarioEntity)
    }

    fun getAllFavoriteMeds(userId: Int): Flow<List<MedicamentoItem>> {
        return medicamentoDAO.getAllFavoritos(userId)
            .map { it -> it.map { it.toDomain() } }
    }

    fun getMedicamentosActivos(userId: Int, fromDate: Date): Flow<List<MedicamentoActivoItem>> {
        return medicamentoActivoDAO.getAllWithMedicamento(userId, fromDate)
            .map { it -> it.map { it.toDomain() } }
    }
}