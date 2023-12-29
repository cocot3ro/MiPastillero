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
import java.util.Calendar

class PillboxViewModel private constructor(context: Context) : ViewModel() {
    private var dbHelper: DBHelper = DBHelper.getInstance(context)

    private var calendarCurrDate =
        Triple<MutableLiveData<Pair<Long, Map<Long, List<Medicamento>>>>, MutableLiveData<Pair<Long, Map<Long, List<Medicamento>>>>, MutableLiveData<Pair<Long, Map<Long, List<Medicamento>>>>>(
            MutableLiveData(
                DateTimeUtils.prevDay(DateTimeUtils.getTodayAsMillis()) to getActivosCalendario(
                    DateTimeUtils.prevDay(DateTimeUtils.getTodayAsMillis())
                )
            ),
            MutableLiveData(DateTimeUtils.getTodayAsMillis() to getActivosCalendario(DateTimeUtils.getTodayAsMillis())),
            MutableLiveData(
                DateTimeUtils.nextDay(DateTimeUtils.getTodayAsMillis()) to getActivosCalendario(
                    DateTimeUtils.nextDay(DateTimeUtils.getTodayAsMillis())
                )
            )
        )

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
    fun getCalendarCurrDate(): Pair<Long, Map<Long, List<Medicamento>>> =
        calendarCurrDate.second.value!!

    /**
     * Establece la fecha que se está mostrando en el calendario
     * @param date fecha en milisegundos
     */
    fun setCalendarCurrDate(date: Long) {
        calendarCurrDate.first.value =
            DateTimeUtils.prevDay(date) to getActivosCalendario(DateTimeUtils.prevDay(date))
        calendarCurrDate.second.value = date to getActivosCalendario(date)
        calendarCurrDate.third.value =
            DateTimeUtils.nextDay(date) to getActivosCalendario(DateTimeUtils.nextDay(date))
    }

    /**
     * Avanza un día en el calendario
     */
    fun calendarNextDay() {
        calendarCurrDate.first.value = calendarCurrDate.second.value
        calendarCurrDate.second.value = calendarCurrDate.third.value
        calendarCurrDate.third.value =
            DateTimeUtils.nextDay(calendarCurrDate.third.value!!.first) to getActivosCalendario(
                DateTimeUtils.nextDay(calendarCurrDate.third.value!!.first)
            )
    }

    /**
     * Retrocede un día en el calendario
     */
    fun calendarPrevDay() {
        calendarCurrDate.third.value = calendarCurrDate.second.value
        calendarCurrDate.second.value = calendarCurrDate.first.value
        calendarCurrDate.first.value =
            DateTimeUtils.prevDay(calendarCurrDate.first.value!!.first) to getActivosCalendario(
                DateTimeUtils.prevDay(calendarCurrDate.first.value!!.first)
            )
    }

    /**
     * Devuelve los medicamentos activos de un día agrupados por hora
     * @param dia día en milisegundos
     */
    private fun getActivosCalendario(dia: Long) = dbHelper.getActivosCalendario(dia)

    fun refreshCalendar() {
        calendarCurrDate.first.value =
            calendarCurrDate.first.value!!.first to getActivosCalendario(calendarCurrDate.first.value!!.first)
        calendarCurrDate.second.value =
            calendarCurrDate.second.value!!.first to getActivosCalendario(calendarCurrDate.second.value!!.first)
        calendarCurrDate.third.value =
            calendarCurrDate.third.value!!.first to getActivosCalendario(calendarCurrDate.third.value!!.first)
    }

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
    fun addActiveMed(medicamento: Medicamento) =
        dbHelper.insertIntoActivos(medicamento).also { if (it) refreshCalendar() }

    /**
     * Elimina un medicamento de la lista de medicamentos activos
     */
    fun deleteActiveMed(medicamento: Medicamento) = dbHelper.deleteFromActivos(medicamento).also { if (it) refreshCalendar() }

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

    fun desmarcarToma(med: Medicamento, hora: Long, dia: Long) =
        dbHelper.desmarcarToma(med, hora, dia)

    fun marcarToma(med: Medicamento, hora: Long, dia: Long) = dbHelper.marcarToma(med, hora, dia)

    /**
     * Consulta el API de la AEMPS para buscar un medicamento
     * @param codNacional código nacional del medicamento
     * @return medicamento si se ha encontrado, null si no
     */
    suspend fun searchMedicamento(codNacional: String): Medicamento.Builder? {
        return withContext(Dispatchers.IO) {
            val apiUrl = "https://cima.aemps.es/cima/rest/medicamento?cn=$codNacional"

            try {
                val response: Response = khttp.get(apiUrl, timeout = 5.0)

                if (response.statusCode != 200) {
                    return@withContext null
                }

                GsonBuilder()
                    .registerTypeAdapter(Medicamento.Builder::class.java, APITypeAdapter())
                    .create()
                    .fromJson(response.text, Medicamento.Builder::class.java)

            } catch (e: Exception) {
                null
            }
        }
    }

    fun comprobarTerminados() = dbHelper.comprobarTerminados()

    fun getHistorial() = dbHelper.getHistorial()

    private fun getEstacion(): Estaciones? {
        return when (Calendar.getInstance().get(Calendar.MONTH)) {
            Calendar.DECEMBER, Calendar.JANUARY, Calendar.FEBRUARY -> Estaciones.INVIERNO
            Calendar.MARCH, Calendar.APRIL, Calendar.MAY -> Estaciones.PRIMAVERA
            Calendar.JUNE, Calendar.JULY, Calendar.AUGUST -> Estaciones.VERANO
            Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER -> Estaciones.OTONHO
            else -> null
        }
    }

    fun getEstacionColor(): Pair<Int, Int> {
        return when (getEstacion()!!) {
            Estaciones.INVIERNO -> R.color.card_header_bg_invierno to R.color.card_bg_invierno
            Estaciones.PRIMAVERA -> R.color.card_header_bg_primavera to R.color.card_bg_primavera
            Estaciones.VERANO -> R.color.card_header_bg_verano to R.color.card_bg_verano
            Estaciones.OTONHO -> R.color.card_header_bg_otoño to R.color.card_bg_otoño
        }
    }

    private fun getEstacion(date: Long): Estaciones? {
        return when (Calendar.getInstance().also { it.timeInMillis = date }.get(Calendar.MONTH)) {
            Calendar.DECEMBER, Calendar.JANUARY, Calendar.FEBRUARY -> Estaciones.INVIERNO
            Calendar.MARCH, Calendar.APRIL, Calendar.MAY -> Estaciones.PRIMAVERA
            Calendar.JUNE, Calendar.JULY, Calendar.AUGUST -> Estaciones.VERANO
            Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER -> Estaciones.OTONHO
            else -> null
        }
    }

    fun getEstacionColor(date: Long): Pair<Int, Int> {
        return when (getEstacion(date)!!) {
            Estaciones.INVIERNO -> R.color.card_header_bg_invierno to R.color.card_bg_invierno
            Estaciones.PRIMAVERA -> R.color.card_header_bg_primavera to R.color.card_bg_primavera
            Estaciones.VERANO -> R.color.card_header_bg_verano to R.color.card_bg_verano
            Estaciones.OTONHO -> R.color.card_header_bg_otoño to R.color.card_bg_otoño
        }
    }

}
