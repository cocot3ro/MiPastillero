package com.example.old.old.old_utils

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.text.format.DateFormat
import com.example.old.R
import java.util.Calendar
import java.util.Date

@Deprecated("old")
object DateTimeUtils {

    const val MILLIS_IN_DAY = 86400000L

    fun hourFromMillis(millis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun minuteFromMillis(millis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return calendar.get(Calendar.MINUTE)
    }

    fun millisToMonth(millis: Long, context: Context): String {
        return when (Calendar.getInstance().also { it.timeInMillis = millis }.get(Calendar.MONTH)) {
            Calendar.JANUARY -> context.getString(R.string.enero)
            Calendar.FEBRUARY -> context.getString(R.string.febrero)
            Calendar.MARCH -> context.getString(R.string.marzo)
            Calendar.APRIL -> context.getString(R.string.abril)
            Calendar.MAY -> context.getString(R.string.mayo)
            Calendar.JUNE -> context.getString(R.string.junio)
            Calendar.JULY -> context.getString(R.string.julio)
            Calendar.AUGUST -> context.getString(R.string.agosto)
            Calendar.SEPTEMBER -> context.getString(R.string.septiembre)
            Calendar.OCTOBER -> context.getString(R.string.octubre)
            Calendar.NOVEMBER -> context.getString(R.string.noviembre)
            Calendar.DECEMBER -> context.getString(R.string.diciembre)
            else -> ""
        }
    }

    fun monthFromMillis(millis: Long): Long {
        return Calendar.getInstance().also { it.timeInMillis = millis }.apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis
    }

    fun millisToYear(millis: Long, context: Context): String {
        return SimpleDateFormat("yyyy", context.resources.configuration.locales[0]).format(
            Date(
                millis
            )
        )
    }

    fun daysBetweenMillis(diaInicio: Long, diaFin: Long): Int {
        return ((diaFin - diaInicio) / (1000 * 60 * 60 * 24)).toInt()
    }

    /**
     * Convierte milisegundos a día de la semana
     * @param millis milisegundos
     */
    fun millisToDayOfWeek(millis: Long, context: Context): Any {
        return when (Calendar.getInstance().also { it.timeInMillis = millis }
            .get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> context.getString(R.string.lunes)
            Calendar.TUESDAY -> context.getString(R.string.martes)
            Calendar.WEDNESDAY -> context.getString(R.string.miercoles)
            Calendar.THURSDAY -> context.getString(R.string.jueves)
            Calendar.FRIDAY -> context.getString(R.string.viernes)
            Calendar.SATURDAY -> context.getString(R.string.sabado)
            Calendar.SUNDAY -> context.getString(R.string.domingo)
            else -> ""
        }
    }

    /**
     * Devuelve la fecha actual en el formato de la configuración regional
     */
    fun getTodayAsString(): String = millisToDate(System.currentTimeMillis())

    /**
     * Devuelve la fecha actual en milisegundos
     */
    fun getTodayAsMillis(): Long = dateToMillis(getTodayAsString())

    fun moveDay(start: Long, days: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = start
        calendar.add(Calendar.DAY_OF_MONTH, days)
        return calendar.timeInMillis
    }

    /**
     * Convierte milisegundos a hora en el formato de la configuración regional
     */
    fun millisToTime(millis: Long): String = SimpleDateFormat.getTimeInstance().format(Date(millis))

    /**
     * Convierte milisegundos a fecha en el formato de la configuración regional
     */
    fun millisToDate(millis: Long): String = SimpleDateFormat.getDateInstance().format(Date(millis))

    /**
     * Convierte hora en el formato de la configuración regional a milisegundos
     */
    fun timeToMillis(hour: String): Long = SimpleDateFormat.getTimeInstance().parse(hour).time

    /**
     * Convierte fecha en el formato de la configuración regional a milisegundos
     */
    fun dateToMillis(date: String): Long = SimpleDateFormat.getDateInstance().parse(date).time

    /**
     * Comprueba si el formato de la hora es de 24 horas
     * @return true si el formato de la hora es de 24 horas, false si es de 12 horas
     */
    fun is24TimeFormat(context: Context) = DateFormat.is24HourFormat(context)

    /**
     * Convierte una fecha a milisegundos
     * @param year año
     * @param monthOfYear mes
     * @param dayOfMonth día
     * @return la representación de la fecha en milisegundos a las 00:00:00:000 (12:00:00:000 AM)
     */
    fun createDate(year: Int, monthOfYear: Int, dayOfMonth: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    /**
     * Convierte una hora a milisegundos
     * @param hourOfDay hora
     * @param minute minuto
     * @return hora en milisegundos
     */
    fun createTime(hourOfDay: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    /**
     * Devuelve la paleta de colores para la estación actual
     * @return Pair<Int, Int> con los colores de la estación
     */
    fun getEstacionColor(): Pair<Int, Int> = getEstacionColor(getTodayAsMillis())

    /**
     * Devuelve la paleta de colores para la estación correspondiente a una fecha
     * @param date fecha
     * @return Pair<Int, Int> con los colores de la estación
     */
    fun getEstacionColor(date: Long): Pair<Int, Int> {
        return when (getEstacion(date)!!) {
            Estaciones.INVIERNO -> R.color.primaryDarkWinter to R.color.primaryWinter
            Estaciones.PRIMAVERA -> R.color.primaryDarkSpring to R.color.primarySpring
            Estaciones.VERANO -> R.color.primaryDarkSummer to R.color.primarySummer
            Estaciones.OTONHO -> R.color.primaryDarkAutumn to R.color.primaryAutumn
        }
    }

    /**
     * Devuelve la estación actual
     * @return estación actual
     * @see Estaciones
     */
    private fun getEstacion(date: Long): Estaciones? {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = date
        }

        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        return when (month) {
            Calendar.JANUARY, Calendar.FEBRUARY -> Estaciones.INVIERNO
            Calendar.APRIL, Calendar.MAY -> Estaciones.PRIMAVERA
            Calendar.JULY, Calendar.AUGUST -> Estaciones.VERANO
            Calendar.OCTOBER, Calendar.NOVEMBER -> Estaciones.OTONHO
            Calendar.MARCH -> if (dayOfMonth < 21) Estaciones.INVIERNO else Estaciones.PRIMAVERA
            Calendar.JUNE -> if (dayOfMonth < 21) Estaciones.PRIMAVERA else Estaciones.VERANO
            Calendar.SEPTEMBER -> if (dayOfMonth < 21) Estaciones.VERANO else Estaciones.OTONHO
            Calendar.DECEMBER -> if (dayOfMonth < 21) Estaciones.OTONHO else Estaciones.INVIERNO
            else -> null
        }
    }
}