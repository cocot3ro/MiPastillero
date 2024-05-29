package com.a23pablooc.proxectofct.data.repositories

import com.a23pablooc.proxectofct.core.CimaImageType
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.database.dao.AgendaDAO
import com.a23pablooc.proxectofct.data.database.dao.HistorialDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoCalendarioDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import com.a23pablooc.proxectofct.data.database.dao.NotificacionDAO
import com.a23pablooc.proxectofct.data.database.dao.UsuarioDAO
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoEntity
import com.a23pablooc.proxectofct.data.database.entities.extensions.toDomain
import com.a23pablooc.proxectofct.data.model.extensions.toDomain
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
    private val notificacionDAO: NotificacionDAO
) {
    // Insert MedicamentoActivoConMedicamento -> insetar medicamento y luego insertar medicamento activo


    fun getAllWithMedicamentosByDiaOrderByHora(dia: Date): Flow<List<MedicamentoCalendarioItem>> {
        return medicamentoCalendarioDAO.getAllWithMedicamentosByDiaOrderByHora(
            UserInfoProvider.currentUser.pkUsuario, dia
        ).map { it -> it.map { it.toDomain() } }
    }

    suspend fun updateMedicamentoCalendario(med: MedicamentoCalendarioItem) {
        medicamentoCalendarioDAO.update(med.toDatabase().medicamentoCalendarioEntity)
    }

    fun getAllFavoriteMeds(): Flow<List<MedicamentoItem>> {
        return medicamentoDAO.getAllFavoritos(UserInfoProvider.currentUser.pkUsuario)
            .map { it -> it.map { it.toDomain() } }
    }

    fun getMedicamentosActivos(fromDate: Date): Flow<List<MedicamentoActivoItem>> {
        return medicamentoActivoDAO.getAllWithMedicamento(
            UserInfoProvider.currentUser.pkUsuario,
            fromDate
        )
            .map { it -> it.map { it.toDomain() } }
    }

    fun findFavoritoByCodNacional(numRegistro: String): MedicamentoEntity? =
        medicamentoDAO.findFavoritoByNumRegistro(
            UserInfoProvider.currentUser.pkUsuario,
            numRegistro
        )
}