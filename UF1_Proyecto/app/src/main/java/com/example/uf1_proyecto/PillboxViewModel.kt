package com.example.uf1_proyecto

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import khttp.responses.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PillboxViewModel private constructor(context: Context) : ViewModel() {
    private var dbHelper: DBHelper = DBHelper.getInstance(context)

    private var calendarCurrDate = MutableLiveData(DateTimeUtils.getTodayAsMillis())

    private var diaryCurrDate = MutableLiveData(DateTimeUtils.getTodayAsMillis())

    companion object {
        /**
         * Instancia única de la clase [PillboxViewModel]
         */
        @Volatile
        private var instance: PillboxViewModel? = null

        /**
         * Devuelve la instancia única de la clase [PillboxViewModel]
         */
        fun getInstance(context: Context): PillboxViewModel = instance ?: synchronized(this) {
            instance ?: PillboxViewModel(context.applicationContext).also { instance = it }
        }

    }

    /**
     * Abre un enlace en el navegador
     * @param context contexto de la aplicación
     * @param url URL a abrir
     */
    fun openURL(context: Context, url: String?): Boolean {
        if (url.isNullOrBlank()) {
            return false
        }

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
        return true
    }

    /**
     * Devuelve la fecha que se está mostrando en el calendario
     */
    fun getCalendarCurrDate(): Long = calendarCurrDate.value!!

    /**
     * Establece la fecha que se está mostrando en el calendario
     * @param date fecha en milisegundos
     */
    fun setCalendarCurrDate(date: Long) {
        calendarCurrDate.value = date
    }

    /**
     * Avanza un día en el calendario
     */
    fun calendarNextDay() {
        calendarCurrDate.value = DateTimeUtils.nextDay(calendarCurrDate.value!!)
    }

    /**
     * Retrocede un día en el calendario
     */
    fun calendarPrevDay() {
        calendarCurrDate.value = DateTimeUtils.prevDay(calendarCurrDate.value!!)
    }

    /**
     * Devuelve los medicamentos activos de un día agrupados por hora
     * @param dia día en milisegundos
     */
    fun getActivosCalendario(dia: Long) = dbHelper.getActivosCalendario(dia)

    /**
     * Devuelve la fecha que se está mostrando en la agenda
     */
    fun getDiaryCurrDate(): Long = diaryCurrDate.value!!

    /**
     * Establece la fecha que se está mostrando en la agenda
     * @param date fecha en milisegundos
     */
    fun setDiaryCurrDate(date: Long) {
        diaryCurrDate.value = date
    }

    /**
     * Avanza un día en la agenda
     */
    fun diaryNextDay() {
        diaryCurrDate.value = DateTimeUtils.nextDay(diaryCurrDate.value!!)
    }

    /**
     * Retrocede un día en la agenda
     */
    fun diaryPrevDay() {
        diaryCurrDate.value = DateTimeUtils.prevDay(diaryCurrDate.value!!)
    }

    /**
     * Devuelve el texto de la entrada de la agenda del dia correspondiente
     */
    fun getDiaryText() = dbHelper.getDiaryEntry(getDiaryCurrDate())

    /**
     * Devuelve todos los medicamentos activos
     */
    fun getActivos() = dbHelper.getActivos()

    /**
     * Devuelve los medicamentos favoritos
     */
    fun getFavoritos() = dbHelper.getFavoritos()

    /**
     * Añade un medicamento a la lista de medicamentos activos
     */
    fun addActiveMed(medicamento: Medicamento) = dbHelper.insertIntoActivos(medicamento)

    /**
     * Elimina un medicamento de la lista de medicamentos activos
     */
    fun deleteActiveMed(medicamento: Medicamento) = dbHelper.deleteFromActivos(medicamento)

    /**
     * Añade un medicamento a la lista de medicamentos favoritos
     */
    fun addFavMed(medicamento: Medicamento) = dbHelper.insertIntoFavoritos(medicamento)

    /**
     * Elimina un medicamento de la lista de medicamentos favoritos
     */
    fun deleteFavMed(medicamento: Medicamento) = dbHelper.deleteFromFavoritos(medicamento)

    /**
     * Inserta una entrada en la agenda
     * @param descripcion descripción de la entrada
     * @return true si se ha insertado correctamente, false si no
     */
    fun insertIntoAgenda(descripcion: String): Boolean {
        return dbHelper.insertIntoAgenda(getDiaryCurrDate(), descripcion)
    }

    fun desmarcarToma(med: Medicamento, hora: Long, dia: Long) = dbHelper.desmarcarToma(med, hora, dia)

    fun marcarToma(med: Medicamento, hora: Long, dia: Long) = dbHelper.marcarToma(med, hora, dia)

    /**
     * Consulta el API de la AEMPS para buscar un medicamento
     * @param codNacional código nacional del medicamento
     * @return medicamento si se ha encontrado, null si no
     */
    suspend fun searchMedicamento(codNacional: String): Medicamento? {
        return withContext(Dispatchers.IO) {
            val apiUrl = "https://cima.aemps.es/cima/rest/medicamento?cn=$codNacional"

            try {
                val response: Response = khttp.get(apiUrl, timeout = 5.0)

                if (response.statusCode != 200) {
                    return@withContext null
                }

                GsonBuilder()
                    .registerTypeAdapter(Medicamento::class.java, APITypeAdapter())
                    .create()
                    .fromJson(response.text, Medicamento::class.java)

            } catch (e: Exception) {
                null
            }
        }
    }

    fun comprobarTerminados() = dbHelper.comprobarTerminados()

    fun getHistorial() = dbHelper.getHistorial()

}
