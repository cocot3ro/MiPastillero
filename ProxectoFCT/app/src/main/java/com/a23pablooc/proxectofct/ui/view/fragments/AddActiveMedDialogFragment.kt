package com.a23pablooc.proxectofct.ui.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatTime
import com.a23pablooc.proxectofct.core.DateTimeUtils.zero
import com.a23pablooc.proxectofct.databinding.FragmentAddActiveMedDialogBinding
import com.a23pablooc.proxectofct.databinding.TimePickerBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.viewholders.AddActiveMedDialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class AddActiveMedDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentAddActiveMedDialogBinding
    private val viewModel: AddActiveMedDialogViewModel by viewModels()

    private lateinit var listener: OnDataEnteredListener

    interface OnDataEnteredListener {
        fun onDataEntered(med: MedicamentoActivoItem)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddActiveMedDialogBinding.inflate(layoutInflater)

        binding.btnSearch.setOnClickListener {
            search()
        }

        binding.btnAddTimer.setOnClickListener {
            addTimer()
        }

        binding.defaultTimePicker.apply {
            btnDeleteTimer.visibility = View.GONE

            btnSelectTime.setOnClickListener { _ ->
                onSelectTime(hour)
            }
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as OnDataEnteredListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement OnDataEnteredListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setView(requireView())
                setTitle(R.string.añadir_medicamento)
                setPositiveButton(R.string.aceptar, null)
                setNegativeButton(R.string.cancelar, null)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun search() {
        val fetchedMed = viewModel.search(binding.codNacional.text.toString())
    }

    private fun addTimer() {
        TimePickerBinding.inflate(layoutInflater, binding.scheduleLayout, true).apply {
            hour.text = Date().apply {
                zero()
            }.formatTime()

            btnSelectTime.setOnClickListener {
                onSelectTime(hour)
            }

            btnDeleteTimer.setOnClickListener {
                binding.scheduleLayout.removeView(root)
            }

            btnSelectTime.performClick()
        }
    }

    private fun onSelectTime(textView: TextView) {
        TimePickerDialog(
            context, { _, hourOfDay, minute ->
                val pickedTime = Calendar.getInstance().apply {
                    time.zero()
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }.time

                textView.text = pickedTime.formatTime()
            },
            0,
            0,
            DateFormat.is24HourFormat(context)
        ).show()
    }
}