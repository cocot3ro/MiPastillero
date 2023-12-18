package com.example.uf1_proyecto

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import com.example.uf1_proyecto.databinding.DialogRefillMedBinding
import com.example.uf1_proyecto.databinding.TimePickerLayoutBinding
import java.util.Calendar

class RefillMedDialog(
    private val context: Context,
    private val medicamento: Medicamento,
    private val listener: OnDataEnteredListener
) {
    interface OnDataEnteredListener {
        fun onDataEntered(medicamento: Medicamento)
    }

    private var _binding: DialogRefillMedBinding? =
        DialogRefillMedBinding.inflate(LayoutInflater.from(context))

    private val binding get() = _binding!!

    private val inputFechaInicio: TextView = binding.dateStart
    private val inputFechaFin: TextView = binding.dateEnd

    private val alertDialog: AlertDialog = AlertDialog.Builder(context)
        .setView(binding.root)
        .setTitle(context.getString(R.string.añadir_medicamento))
        .setPositiveButton(context.getString(R.string.aceptar), null)
        .setNegativeButton(context.getString(R.string.cancelar), null)
        .create()

    init {
        alertDialog.setOnShowListener {
            setupPositiveButton()
        }

        addTimePicker(false)

        binding.btnAddTimer
            .setOnClickListener { addTimePicker(true) }

        val addDatePickerListener = OnClickListener { view ->
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, monthOfYear, dayOfMonth ->
                    when (view.id) {
                        R.id.btn_date_picker1 -> inputFechaInicio.text =
                            DateTimeUtils.millisToDate(
                                DateTimeUtils.createDate(
                                    year, monthOfYear, dayOfMonth
                                )
                            )

                        R.id.btn_date_picker2 -> inputFechaFin.text = DateTimeUtils.millisToDate(
                            DateTimeUtils.createDate(
                                year, monthOfYear, dayOfMonth
                            )
                        )
                    }
                },
                // Establece la fecha actual como predeterminada
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        binding.btnDatePicker1.setOnClickListener(addDatePickerListener)
        binding.btnDatePicker2.setOnClickListener(addDatePickerListener)

        inputFechaInicio.text = DateTimeUtils.getTodayAsString()
        inputFechaFin.text = DateTimeUtils.getTodayAsString()
    }

    private fun addTimePicker(showAfterAdd: Boolean) {
        val timerBinding = TimePickerLayoutBinding.inflate(
            LayoutInflater.from(context),
            binding.scheduleLayout,
            true
        )

        timerBinding.timerHour.text = DateTimeUtils.millisToTime(-3600000) // 0:00:00 - 12:00:00 AM

        timerBinding.timePicker.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                context, { _, hourOfDay, minute ->
                    val time = DateTimeUtils.millisToTime(
                        DateTimeUtils.createTime(
                            hourOfDay, minute
                        )
                    )
                    timerBinding.timerHour.text = time
                },
                // Establece las 00:00 como hora predeterminada
                0,
                0,
                DateTimeUtils.is24TimeFormat(context)
            )

            timePickerDialog.show()
        }

        timerBinding.deleteTimer.setOnClickListener {
            binding.scheduleLayout.removeView(timerBinding.root)
        }

        if (showAfterAdd) {
            timerBinding.timePicker.performClick()
        }
    }

    private fun setupPositiveButton() {
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (!validateForm()) {
                return@setOnClickListener
            }

            val nombre = medicamento.nombre!!
            val fechaInicio = DateTimeUtils.dateToMillis(inputFechaInicio.text.toString())
            val fechaFin = DateTimeUtils.dateToMillis(inputFechaFin.text.toString())
            val horario = getSchedule()

            alertDialog.dismiss()
            val builder = Medicamento.Builder()
                .setNombre(nombre)
                .setFechaInicio(fechaInicio)
                .setFechaFin(fechaFin)
                .setHorario(horario)
            listener.onDataEntered(builder.build())
        }
    }

    private fun validateForm(): Boolean {
        if ((DateTimeUtils.dateToMillis(inputFechaInicio.text.toString()) > DateTimeUtils.dateToMillis(
                inputFechaFin.text.toString()
            ) || (DateTimeUtils.dateToMillis(inputFechaFin.text.toString()) < DateTimeUtils.getTodayAsMillis()))
        ) {
            Toast.makeText(
                context, context.getString(R.string.fecha_invalida), Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (getSchedule().isEmpty()) {
            Toast.makeText(
                context, context.getString(R.string.sin_horario), Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (getSchedule().size < binding.scheduleLayout.childCount) {
            Toast.makeText(
                context, context.getString(R.string.horario_invalido), Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    private fun getSchedule(): Set<Long> {
        val horario = sortedSetOf<Long>()

        for (child in (binding.scheduleLayout.children)) {
            val time = child.findViewById<TextView>(R.id.timer_hour).text.toString()
            horario.add(DateTimeUtils.timeToMillis(time))
        }

        return horario
    }

    fun show() {
        alertDialog.show()
    }

}