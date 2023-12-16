package com.example.uf1_proyecto

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.text.format.DateFormat
import java.util.Calendar
import java.util.Date

object DateTimeUtils {

    /**
     * Devuelve el día de la semana actual
     */
    fun getTodayAsDayOfWeek(context: Context): String {
        return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
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

    fun millisToYear(millis: Long, context: Context): String {
        return SimpleDateFormat("yyyy", context.resources.configuration.locales[0]).format(Date(millis))
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
     * @return fecha en milisegundos
     */
    fun createDate(year: Int, monthOfYear: Int, dayOfMonth: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
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
     * Devuelve el día anterior a la fecha dada
     * @param date fecha
     * @return día anterior a la fecha dada
     */
    fun prevDay(date: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return calendar.timeInMillis
    }

    /**
     * Devuelve el día siguiente a la fecha dada
     * @param date fecha
     * @return día siguiente a la fecha dada
     */
    fun nextDay(date: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return calendar.timeInMillis
    }
}