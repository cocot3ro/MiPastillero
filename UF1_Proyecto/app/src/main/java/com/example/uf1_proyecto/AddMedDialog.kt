package com.example.uf1_proyecto

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class AddMedDialog(private val context: Context, private val listener: OnDataEnteredListener) {

    interface OnDataEnteredListener {
        fun onDataEntered(medicamento: Medicamento)
    }

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    private val dialogView: View =
        LayoutInflater.from(context).inflate(R.layout.dialog_add_med, null)

    private val inputCodNacional: EditText = dialogView.findViewById(R.id.codNacional)
    private val inputNombre: EditText = dialogView.findViewById(R.id.nombre)
    private val inputFavorite: CheckBox = dialogView.findViewById(R.id.save_as_favorite)
    private val inputFechaInicio: TextView = dialogView.findViewById(R.id.date_start)
    private val inputFechaFin: TextView = dialogView.findViewById(R.id.date_end)
    private var fichaTecnica: String? = null
    private var prospecto: String? = null

    private val alertDialog: AlertDialog = AlertDialog.Builder(context)
        .setView(dialogView)
        .setTitle(context.getString(R.string.add_medicament))
        .setPositiveButton(context.getString(R.string.accept), null)
        .setNegativeButton(context.getString(R.string.cancel), null)
        .create()

    init {
        _pillboxViewModel = PillboxViewModel.getInstance(context)

        alertDialog.setOnShowListener {
            setupPositiveButton()
        }

        addTimePicker()

        dialogView.findViewById<ImageButton>(R.id.btn_search).setOnClickListener {
            if (inputCodNacional.text.isNullOrBlank()) {
                return@setOnClickListener
            }

            val searchingToast = Toast.makeText(
                context, context.getString(R.string.searching), Toast.LENGTH_LONG
            ).also { it.show() }

            GlobalScope.launch(Dispatchers.Main) {
                val codNacional = inputCodNacional.text.toString()
                val index = codNacional.indexOf(".")

                val medicamento = if (index != -1) {
                    pillboxViewModel.searchMedicamento(codNacional.substring(0, index))
                } else {
                    pillboxViewModel.searchMedicamento(codNacional)
                }

                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    if (medicamento == null) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.codNacional_not_found),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        inputNombre.setText(medicamento.nombre)
                        fichaTecnica = medicamento.fichaTecnica
                        prospecto = medicamento.prospecto
                    }
                }
            }

        }

        dialogView.findViewById<ImageButton>(R.id.btn_help).setOnClickListener {
            Toast.makeText(context, context.getString(R.string.codNacional_help), Toast.LENGTH_LONG)
                .show()
        }

        dialogView.findViewById<ImageButton>(R.id.btn_add_timer)
            .setOnClickListener { addTimePicker() }

        val listener = OnClickListener { view ->
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, monthOfYear, dayOfMonth ->
                    when (view.id) {
                        R.id.btn_date_picker1 -> inputFechaInicio.text =
                            pillboxViewModel.millisToDate(
                                pillboxViewModel.createDate(
                                    year, monthOfYear, dayOfMonth
                                )
                            )

                        R.id.btn_date_picker2 -> inputFechaFin.text = pillboxViewModel.millisToDate(
                            pillboxViewModel.createDate(
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

        dialogView.findViewById<ImageButton>(R.id.btn_date_picker1).setOnClickListener(listener)
        dialogView.findViewById<ImageButton>(R.id.btn_date_picker2).setOnClickListener(listener)

        inputFechaInicio.text = pillboxViewModel.getTodayAsString()
        inputFechaFin.text = pillboxViewModel.getTodayAsString()

    }

    private fun validateForm(): Boolean {
        if (inputNombre.text.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.empty_name), Toast.LENGTH_LONG)
                .show()
            return false
        }

        if (pillboxViewModel.dateToMillis(inputFechaInicio.text.toString()) > pillboxViewModel.dateToMillis(
                inputFechaFin.text.toString()
            )
        ) {
            Toast.makeText(
                context, context.getString(R.string.invalid_date), Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (getSchedule().isEmpty()) {
            Toast.makeText(
                context, context.getString(R.string.no_schedule), Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (getSchedule().size < dialogView.findViewById<LinearLayout>(R.id.schedule_layout).childCount) {
            Toast.makeText(
                context, context.getString(R.string.invalid_schedule), Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    private fun getSchedule(): List<Long> {
        val horario = mutableListOf<Long>()

        for (child in (dialogView.findViewById<LinearLayout>(R.id.schedule_layout).children)) {
            val time = child.findViewById<TextView>(R.id.timer_hour).text.toString()
            if (!horario.contains(pillboxViewModel.hourToMillis(time))) {
                horario.add(pillboxViewModel.hourToMillis(time))
            }
        }

        return horario
    }

    private fun addTimePicker() {
        val inflater = LayoutInflater.from(context)
        val scheduleLayout = dialogView.findViewById<LinearLayout>(R.id.schedule_layout)

        val timer = inflater.inflate(
            R.layout.time_picker_layout, scheduleLayout, false
        ) as LinearLayout

        timer.findViewById<TextView>(R.id.timer_hour).text = pillboxViewModel.millisToHour(-3600000)

        timer.findViewById<ImageButton>(R.id.timePicker).setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                context, { _, hourOfDay, minute ->
                    val time = pillboxViewModel.millisToHour(
                        pillboxViewModel.createHour(
                            hourOfDay, minute
                        )
                    )
                    timer.findViewById<TextView>(R.id.timer_hour).text = time
                }, 0, 0, pillboxViewModel.is24HourFormat(context)
            )

            timePickerDialog.show()
        }

        timer.findViewById<ImageButton>(R.id.delete_timer).setOnClickListener {
            scheduleLayout.removeView(timer)
        }

        scheduleLayout.addView(timer)
    }

    private fun setupPositiveButton() {
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (!validateForm()) {
                return@setOnClickListener
            }

            val nombre = inputNombre.text.toString()
            var codNacional = inputCodNacional.text.toString()
            val index = codNacional.indexOf(".")
            if (index != -1) {
                codNacional = codNacional.substring(0, index)
            }
            val fichaTecnica = fichaTecnica
            val prospecto = prospecto
            val fechaInicio = pillboxViewModel.dateToMillis(inputFechaInicio.text.toString())
            val fechaFin = pillboxViewModel.dateToMillis(inputFechaFin.text.toString())
            val horario = getSchedule()
            val isFavorite = inputFavorite.isChecked

            alertDialog.dismiss()
            listener.onDataEntered(
                Medicamento(
                    nombre,
                    codNacional.toInt(),
                    fichaTecnica,
                    prospecto,
                    fechaInicio,
                    fechaFin,
                    horario,
                    isFavorite
                )
            )
        }
    }

    fun show() {
        alertDialog.show()
    }

}
