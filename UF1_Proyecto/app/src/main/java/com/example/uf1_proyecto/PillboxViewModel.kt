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

    // TODO: Comentar

    private var dbHelper: DBHelper = DBHelper.getInstance(context)

    private var calendarData: MutableLiveData<Triple<Pair<Long, Map<Long, List<Medicamento>>>, Pair<Long, Map<Long, List<Medicamento>>>, Pair<Long, Map<Long, List<Medicamento>>>>> =
        MutableLiveData(
            Triple(
                DateTimeUtils.prevDay(DateTimeUtils.getTodayAsMillis()) to getActivosCalendario(
                    DateTimeUtils.prevDay(DateTimeUtils.getTodayAsMillis())
                ),

                DateTimeUtils.getTodayAsMillis() to getActivosCalendario(DateTimeUtils.getTodayAsMillis()),

                DateTimeUtils.nextDay(DateTimeUtils.getTodayAsMillis()) to getActivosCalendario(
                    DateTimeUtils.nextDay(DateTimeUtils.getTodayAsMillis())
                )
            )
        )

    private var diaryData: MutableLiveData<Triple<Pair<Long, String>, Pair<Long, String>, Pair<Long, String>>> =
        MutableLiveData(
            Triple(
                DateTimeUtils.prevDay(DateTimeUtils.getTodayAsMillis()) to (getDiaryText(
                    DateTimeUtils.prevDay(DateTimeUtils.getTodayAsMillis())
                )),

                DateTimeUtils.getTodayAsMillis() to (getDiaryText(DateTimeUtils.getTodayAsMillis())),

                DateTimeUtils.nextDay(DateTimeUtils.getTodayAsMillis()) to (getDiaryText(
                    DateTimeUtils.nextDay(DateTimeUtils.getTodayAsMillis())
                ))
            )
        )

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

    fun getCalendarPrevDayData(): Pair<Long, Map<Long, List<Medicamento>>> =
        calendarData.value!!.first

    /**
     * Devuelve la fecha que se está mostrando en el calendario
     */
    fun getCalendarCurrDayData(): Pair<Long, Map<Long, List<Medicamento>>> =
        calendarData.value!!.second

    fun getCalendarNextDayData(): Pair<Long, Map<Long, List<Medicamento>>> =
        calendarData.value!!.third

    /**
     * Establece la fecha que se está mostrando en el calendario
     * @param date fecha en milisegundos
     */
    fun setCalendarCurrDate(date: Long) {
        calendarData.value = Triple(
            DateTimeUtils.prevDay(date) to getActivosCalendario(DateTimeUtils.prevDay(date)),
            date to getActivosCalendario(date),
            DateTimeUtils.nextDay(date) to getActivosCalendario(DateTimeUtils.nextDay(date))
        )
    }

    /**
     * Avanza un día en el calendario
     */
    fun calendarMoveForward() {
        calendarData.value = Triple(
            calendarData.value!!.second,
            calendarData.value!!.third,
            DateTimeUtils.nextDay(calendarData.value!!.third.first) to getActivosCalendario(
                DateTimeUtils.nextDay(calendarData.value!!.third.first)
            )
        )

    }

    /**
     * Retrocede un día en el calendario
     */
    fun calendarMoveBackward() {
        calendarData.value = Triple(
            DateTimeUtils.prevDay(calendarData.value!!.first.first) to getActivosCalendario(
                DateTimeUtils.prevDay(calendarData.value!!.first.first)
            ),
            calendarData.value!!.first,
            calendarData.value!!.second
        )
    }

    /**
     * Devuelve los medicamentos activos de un día agrupados por hora
     * @param dia día en milisegundos
     */
    private fun getActivosCalendario(dia: Long) = dbHelper.getActivosCalendario(dia)

    private fun refreshCalendar() {
        calendarData.value = Triple(
            calendarData.value!!.first.first to getActivosCalendario(calendarData.value!!.first.first),
            calendarData.value!!.second.first to getActivosCalendario(calendarData.value!!.second.first),
            calendarData.value!!.third.first to getActivosCalendario(calendarData.value!!.third.first)
        )
    }

    fun getDiaryPrevDayData(): Pair<Long, String> = diaryData.value!!.first

    fun getDiaryCurrDayData(): Pair<Long, String> = diaryData.value!!.second

    fun getDiaryNextDayData(): Pair<Long, String> = diaryData.value!!.third

    /**
     * Establece la fecha que se está mostrando en la agenda
     * @param date fecha en milisegundos
     */
    fun setDiaryCurrDate(date: Long) {
        diaryData.value = Triple(
            DateTimeUtils.prevDay(date) to getDiaryText(DateTimeUtils.prevDay(date)),
            date to getDiaryText(date),
            DateTimeUtils.nextDay(date) to getDiaryText(DateTimeUtils.nextDay(date))
        )
    }

    /**
     * Avanza un día en la agenda
     */
    fun diaryMoveForward() {
        diaryData.value = Triple(
            diaryData.value!!.second,
            diaryData.value!!.third,
            DateTimeUtils.nextDay(diaryData.value!!.third.first) to getDiaryText(
                DateTimeUtils.nextDay(diaryData.value!!.third.first)
            )
        )
    }

    /**
     * Retrocede un día en la agenda
     */
    fun diaryMoveBackward() {
        diaryData.value = Triple(
            DateTimeUtils.prevDay(diaryData.value!!.first.first) to getDiaryText(
                DateTimeUtils.prevDay(diaryData.value!!.first.first)
            ),
            diaryData.value!!.first,
            diaryData.value!!.second
        )
    }

    private fun refreshDiary() {
        diaryData.value = Triple(
            diaryData.value!!.first.first to getDiaryText(diaryData.value!!.first.first),
            diaryData.value!!.second.first to getDiaryText(diaryData.value!!.second.first),
            diaryData.value!!.third.first to getDiaryText(diaryData.value!!.third.first)
        )
    }

    /**
     * Devuelve el texto de la entrada de la agenda del dia correspondiente
     */
    private fun getDiaryText(dia: Long) = dbHelper.getDiaryEntry(dia) ?: ""

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
    fun deleteActiveMed(medicamento: Medicamento) =
        dbHelper.deleteFromActivos(medicamento).also { if (it) refreshCalendar() }

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
    fun insertIntoAgenda(date: Long, descripcion: String): Boolean {
        return dbHelper.insertIntoAgenda(date, descripcion)
            .also { if (it) refreshDiary() }
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
            Estaciones.INVIERNO -> R.color.primaryDarkWinter to R.color.primaryWinter
            Estaciones.PRIMAVERA -> R.color.primaryDarkSpring to R.color.primarySpring
            Estaciones.VERANO -> R.color.primaryDarkSummer to R.color.primarySummer
            Estaciones.OTONHO -> R.color.primaryDarkAutumn to R.color.primaryAutumn
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
            Estaciones.INVIERNO -> R.color.primaryDarkWinter to R.color.primaryWinter
            Estaciones.PRIMAVERA -> R.color.primaryDarkSpring to R.color.primarySpring
            Estaciones.VERANO -> R.color.primaryDarkSummer to R.color.primarySummer
            Estaciones.OTONHO -> R.color.primaryDarkAutumn to R.color.primaryAutumn
        }
    }

}
