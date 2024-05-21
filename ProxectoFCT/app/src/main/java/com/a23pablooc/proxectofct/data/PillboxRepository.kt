package com.a23pablooc.proxectofct.data

import com.a23pablooc.proxectofct.core.CimaImageType
import com.a23pablooc.proxectofct.data.database.dao.AgendaDAO
import com.a23pablooc.proxectofct.data.database.dao.HistorialDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoCalendarioDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoFavoritoDAO
import com.a23pablooc.proxectofct.data.database.dao.NotificacionDAO
import com.a23pablooc.proxectofct.data.database.dao.UsuarioDAO
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoCalendarioAndMedicamento
import com.a23pablooc.proxectofct.data.network.CimaService
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class PillboxRepository @Inject constructor(
    private val usuarioDAO: UsuarioDAO,
    private val medicamentoDAO: MedicamentoDAO,
    private val medicamentoActivoDAO: MedicamentoActivoDAO,
    private val medicamentoFavoritoDAO: MedicamentoFavoritoDAO,
    private val medicamentoCalendarioDAO: MedicamentoCalendarioDAO,
    private val agendaDAO: AgendaDAO,
    private val historialDAO: HistorialDAO,
    private val notificacionDAO: NotificacionDAO,
    private val cimaService: CimaService
) {
//    suspend fun getAll() = medicamentoDAO.getAll()

    suspend fun downloadImage(imageType: CimaImageType, nregistro: String, imgResource: String): ByteArray? {
        return cimaService.getMedImage(imageType, nregistro, imgResource)
    }

    fun getAllWithMedicamentosByDiaOrderByHora(
        userId: Int,
        dia: Date
    ): Flow<List<MedicamentoCalendarioAndMedicamento>> =
        medicamentoCalendarioDAO.getAllWithMedicamentosByDiaOrderByHora(userId, dia)

    /*
    TODO:
        Insert all,
        get from api...
        Insert MedicamentoActivoConMedicamento -> insetar medicamento y luego insertar medicamento activo
     */
}