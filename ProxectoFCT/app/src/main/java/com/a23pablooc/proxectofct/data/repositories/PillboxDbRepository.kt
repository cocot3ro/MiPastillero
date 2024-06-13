package com.a23pablooc.proxectofct.data.repositories

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.database.dao.AgendaDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoWithNotificacionDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoWithMedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.NotificacionDAO
import com.a23pablooc.proxectofct.data.database.dao.UsuarioDAO
import com.a23pablooc.proxectofct.data.database.entities.MedicamentoActivoEntity
import com.a23pablooc.proxectofct.data.database.entities.extensions.toDomain
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.domain.model.extensions.toDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PillboxDbRepository @Inject constructor(
    private val usuarioDAO: UsuarioDAO,
    private val medicamentoDAO: MedicamentoDAO,
    private val medicamentoActivoDAO: MedicamentoActivoDAO,
    private val agendaDAO: AgendaDAO,
    private val notificacionDAO: NotificacionDAO,
    private val medicamentoActivoWithNotificacionDAO: MedicamentoActivoWithNotificacionDAO,
    private val medicamentoWithMedicamentoActivoDAO: MedicamentoWithMedicamentoActivoDAO,
    private val userInfoProvider: UserInfoProvider
) {

    suspend fun updateMed(med: MedicamentoActivoItem) {
        medicamentoActivoDAO.update(med.toDatabase().medicamentosActivos[0])
    }

    fun getAllFavoriteMeds(): Flow<List<MedicamentoItem>> {
        return medicamentoDAO.getAllFavoritos(userInfoProvider.currentUser.pkUsuario)
            .map { list -> list.map { med -> med.toDomain() } }
    }

    fun getMedicamentosActivos(): Flow<List<MedicamentoActivoItem>> {
        return medicamentoWithMedicamentoActivoDAO.getAll(
            userInfoProvider.currentUser.pkUsuario
        ).map { list ->
            list.map { medWithMedActivo ->
                medWithMedActivo.medicamentosActivos.map { medActivo ->
                    medActivo.toDomain(medWithMedActivo.medicamento)
                }
            }.flatten()
        }
    }

    fun findMedicamentoByCodNacional(userId: Long, codNacional: Long): MedicamentoItem? {
        return medicamentoDAO.findByCodNacional(userId, codNacional)?.toDomain()
    }

    suspend fun addMedicamentoActivo(med: MedicamentoActivoEntity) {
        medicamentoActivoDAO.insert(med)
    }

    suspend fun addMedicamento(med: MedicamentoItem): Long {
        return medicamentoDAO.upsert(med.toDatabase())
    }

    suspend fun updateMedicamento(medicamento: MedicamentoItem) {
        medicamentoDAO.update(medicamento.toDatabase())
    }

    fun getUsers(): Flow<List<UsuarioItem>> {
        return usuarioDAO.getAll().map { list -> list.map { user -> user.toDomain() } }
    }

    suspend fun createUser(user: UsuarioItem): Long {
        return usuarioDAO.insert(user.toDatabase())
    }

    suspend fun deleteUser(user: UsuarioItem) {
        usuarioDAO.delete(user.toDatabase())
    }

    suspend fun updateUser(user: UsuarioItem) {
        usuarioDAO.update(user.toDatabase())
    }
}