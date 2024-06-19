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
import com.a23pablooc.proxectofct.domain.model.AgendaItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.model.NotificacionItem
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.domain.model.extensions.toDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
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

    fun getAllFavoriteMedsFlow(): Flow<List<MedicamentoItem>> {
        return medicamentoDAO.getAllFavoritos(userInfoProvider.currentUser.pkUsuario)
            .map { list -> list.map { med -> med.toDomain() } }
    }

    fun getMedicamentosActivosFlow(): Flow<List<MedicamentoActivoItem>> {
        return medicamentoWithMedicamentoActivoDAO.getAllFlow(
            userInfoProvider.currentUser.pkUsuario
        ).map { list ->
            list.map { medWithMedActivo ->
                medWithMedActivo.medicamentosActivos.map { medActivo ->
                    medActivo.toDomain(medWithMedActivo.medicamento)
                }
            }.flatten()
        }
    }

    suspend fun getMedicamentosActivos(user: UsuarioItem): List<MedicamentoActivoItem> {
        return medicamentoWithMedicamentoActivoDAO.getAll(user.pkUsuario).map { medWithMedActivo ->
            medWithMedActivo.medicamentosActivos.map { medActivo ->
                medActivo.toDomain(medWithMedActivo.medicamento)
            }
        }.flatten()
    }

    fun findMedicamentoByCodNacional(userId: Long, codNacional: Long): MedicamentoItem? {
        return medicamentoDAO.findByCodNacional(userId, codNacional)?.toDomain()
    }

    suspend fun addMedicamentoActivo(med: MedicamentoActivoEntity) {
        medicamentoActivoDAO.insert(med)
    }

    suspend fun upsertMedicamento(med: MedicamentoItem): Long {
        return medicamentoDAO.upsert(med.toDatabase())
    }

    suspend fun updateMedicamento(medicamento: MedicamentoItem) {
        medicamentoDAO.update(medicamento.toDatabase())
    }

    fun getUsersFlow(): Flow<List<UsuarioItem>> {
        return usuarioDAO.getAllFlow().map { list -> list.map { user -> user.toDomain() } }
    }

    suspend fun getUsers(): List<UsuarioItem> {
        return usuarioDAO.getAll().map { user -> user.toDomain() }
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

    fun getDiaryFlow(date: Date): Flow<List<AgendaItem>> {
        return agendaDAO.getByFecha(userInfoProvider.currentUser.pkUsuario, date.time)
            .map { list ->
                list.map { agenda -> agenda.toDomain() }
            }
    }

    suspend fun saveDiaryEntry(item: AgendaItem) {
        agendaDAO.upsert(item.toDatabase())
    }

    suspend fun deleteDiaryEntry(item: AgendaItem) {
        agendaDAO.delete(item.toDatabase())
    }

    suspend fun getNotificaciones(userId: Long, med: MedicamentoActivoItem): List<NotificacionItem> {
        return medicamentoActivoWithNotificacionDAO.getAll(
            userId,
            med.pkMedicamentoActivo
        ).map { medWithNotif ->
            medWithNotif.notificaciones.map { notif ->
                notif.toDomain(med)
            }
        }.flatten().distinctBy { it.pkNotificacion }
    }

    suspend fun insertNotificacion(noti: NotificacionItem) {
        notificacionDAO.insert(noti.toDatabase())
    }

    suspend fun deleteNotificacion(notificacion: NotificacionItem) {
        notificacionDAO.delete(notificacion.toDatabase())
    }
}