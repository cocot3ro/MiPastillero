package com.example.uf1_proyecto

import android.app.AlertDialog
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class DataInputDialog(private val context: Context, private val listener: OnDataEnteredListener) {

    interface OnDataEnteredListener {
        fun onDataEntered(medicamento: Medicamento?)
    }

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    private val dialogView: View =
        LayoutInflater.from(context).inflate(R.layout.dialog_data_input, null)

    private val inputCodNacional: EditText = dialogView.findViewById(R.id.codNacional)
    private val inputNombre: EditText = dialogView.findViewById(R.id.nombre)

    private val alertDialog: AlertDialog = AlertDialog.Builder(context)
        .setView(dialogView)
        .setTitle(context.getString(R.string.add_medicament))
        .setPositiveButton(context.getString(R.string.accept)) { _, _ ->
            val codNacional = inputCodNacional.text.toString()
            val nombre = inputNombre.text.toString()

            val horario = mutableListOf<Long>()
            for (i in 0 until (dialogView.findViewById<LinearLayout>(R.id.schedule_layout).childCount)) {
                val timer =
                    dialogView.findViewById<LinearLayout>(R.id.schedule_layout).getChildAt(i)
                val hour = timer.findViewById<EditText>(R.id.timerHour).text.toString()
                if (!is24HourFormat(context)) {
                    if (timer.findViewById<RadioGroup>(R.id.time_format_grp).checkedRadioButtonId == -1) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.no_time_format),
                            Toast.LENGTH_LONG
                        ).show()
                        return@setPositiveButton
                    }

                    if (timer.findViewById<RadioButton>(R.id.radio_pm).isChecked) {
                        hour.toInt() + 12
                    }
                }
                val minute = timer.findViewById<EditText>(R.id.timerMinute).text.toString()
                val second = timer.findViewById<EditText>(R.id.timerSecond).text.toString()
                val time = "$hour:$minute:$second"
                horario.add(pillboxViewModel.hourToMillis(time))
            }

            listener.onDataEntered(null)

        }
        .setNegativeButton(context.getString(R.string.cancel), null)
        .create()

    init {
        addTimePicker()

        _pillboxViewModel = PillboxViewModel.getInstance(context)

        dialogView.findViewById<ImageButton>(R.id.btn_search).setOnClickListener {
            val medicamento = pillboxViewModel.searchMedicamento(inputCodNacional.text.toString())

            if (medicamento == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.codNacional_not_found),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                inputNombre.setText(medicamento.nombre)
            }
        }

        dialogView.findViewById<ImageButton>(R.id.btn_help).setOnClickListener {
            Toast.makeText(context, context.getString(R.string.codNacional_help), Toast.LENGTH_LONG)
                .show()
        }

        dialogView.findViewById<ImageButton>(R.id.btn_add_timer)
            .setOnClickListener { addTimePicker() }
    }

    private fun addTimePicker() {
        val inflater = LayoutInflater.from(context)
        val scheduleLayout = dialogView.findViewById<LinearLayout>(R.id.schedule_layout)

        val timer = inflater.inflate(
            R.layout.time_picker_layout,
            scheduleLayout,
            false
        ) as LinearLayout

        if (is24HourFormat(context)) {
            timer.removeView(timer.findViewById(R.id.time_format_layout))
        }

        val hourTime = timer.findViewById<EditText>(R.id.timerHour)
        timer.findViewById<ImageButton>(R.id.hourIncrement).setOnClickListener {
            if (is24HourFormat(context)) {
                if (hourTime.text.toString().toInt() == 23) {
                    hourTime.setText("0")
                } else {
                    hourTime.setText((hourTime.text.toString().toInt() + 1).toString())
                }
            } else {
                if (hourTime.text.toString().toInt() == 12) {
                    hourTime.setText("1")
                } else {
                    hourTime.setText((hourTime.text.toString().toInt() + 1).toString())
                }
            }
        }

        timer.findViewById<ImageButton>(R.id.hourDecrement).setOnClickListener {
            if (is24HourFormat(context)) {
                if (hourTime.text.toString().toInt() == 0) {
                    hourTime.setText("23")
                } else {
                    hourTime.setText((hourTime.text.toString().toInt() - 1).toString())
                }
            } else {
                if (hourTime.text.toString().toInt() == 1) {
                    hourTime.setText("12")
                } else {
                    hourTime.setText((hourTime.text.toString().toInt() - 1).toString())
                }
            }
        }

        val minuteTime = timer.findViewById<EditText>(R.id.timerMinute)
        timer.findViewById<ImageButton>(R.id.minuteIncrement).setOnClickListener {
            if (minuteTime.text.toString().toInt() == 59) {
                minuteTime.setText("0")
            } else {
                minuteTime.setText((minuteTime.text.toString().toInt() + 1).toString())
            }
        }

        timer.findViewById<ImageButton>(R.id.minuteDecrement).setOnClickListener {
            if (minuteTime.text.toString().toInt() == 0) {
                minuteTime.setText("59")
            } else {
                minuteTime.setText((minuteTime.text.toString().toInt() - 1).toString())
            }
        }

        val secondTime = timer.findViewById<EditText>(R.id.timerSecond)
        timer.findViewById<ImageButton>(R.id.secondIncrement).setOnClickListener {
            if (secondTime.text.toString().toInt() == 59) {
                secondTime.setText("0")
            } else {
                secondTime.setText((secondTime.text.toString().toInt() + 1).toString())
            }
        }

        timer.findViewById<ImageButton>(R.id.secondDecrement).setOnClickListener {
            if (secondTime.text.toString().toInt() == 0) {
                secondTime.setText("59")
            } else {
                secondTime.setText((secondTime.text.toString().toInt() - 1).toString())
            }
        }

        timer.findViewById<ImageButton>(R.id.delete_timer).setOnClickListener {
            scheduleLayout.removeView(timer)
        }

        scheduleLayout.addView(timer)
    }

    private fun is24HourFormat(context: Context): Boolean {
        return DateFormat.is24HourFormat(context)
    }

    fun show() {
        alertDialog.show()
    }

}
