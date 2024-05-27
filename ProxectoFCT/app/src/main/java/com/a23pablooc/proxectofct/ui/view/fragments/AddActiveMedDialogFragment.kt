package com.a23pablooc.proxectofct.ui.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatTime
import com.a23pablooc.proxectofct.core.DateTimeUtils.zero
import com.a23pablooc.proxectofct.databinding.FragmentAddActiveMedDialogBinding
import com.a23pablooc.proxectofct.databinding.TimePickerBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.ui.view.viewholders.AddActiveMedDialogViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class AddActiveMedDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentAddActiveMedDialogBinding
    private val viewModel: AddActiveMedDialogViewModel by viewModels()

    private lateinit var listener: OnDataEnteredListener

    private val pickMedia = registerForActivityResult(PickVisualMedia()) {
        it?.let {
            Glide.with(requireContext()).load(it).into(binding.img)
        }
    }

    interface OnDataEnteredListener {
        fun onDataEntered(med: MedicamentoActivoItem)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddActiveMedDialogBinding.inflate(layoutInflater)

        binding.imgLayout.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

//      TODO: Input dosis

        binding.btnHelp.setOnClickListener {
            showHelp()
        }

        binding.btnSearch.setOnClickListener {
            search()
        }

        binding.btnAddTimer.setOnClickListener {
            addTimer(true)
        }

        addTimer(false)

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
                setPositiveButton(R.string.aceptar) { dialogInterface, i ->

                }
                setNegativeButton(R.string.cancelar, null)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun search() {
        try {
            Toast.makeText(requireContext(), R.string.buscando, Toast.LENGTH_SHORT).show()

            binding.progressBar.visibility = View.VISIBLE

            val codNacional = binding.codNacional.text.toString()

            val fetchedMed: MedicamentoItem? = viewModel.search(codNacional)

            if (fetchedMed == null) {
                Toast.makeText(
                    requireContext(),
                    R.string.codigo_nacional_no_encontrado,
                    Toast.LENGTH_SHORT
                )
                    .show()
                return
            }

            binding.codNacional.apply {
                isEnabled = false
                setText(codNacional)
                binding.codNacionalLock.visibility = View.VISIBLE
            }

            binding.nombre.apply {
                isEnabled = false
                setText(fetchedMed.nombre)
                binding.nombreLock.visibility = View.VISIBLE
            }

            Glide.with(requireContext()).load(fetchedMed.imagen).into(binding.img)

        } catch (e: IllegalArgumentException) {
            Toast.makeText(requireContext(), R.string.codigo_nacional_no_valido, Toast.LENGTH_SHORT)
                .show()
            Log.e(
                "AddActiveMedDialogFragment.search",
                "IllegalArgumentException: ${e.stackTraceToString()}"
            )
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            Log.e("AddActiveMedDialogFragment.search", "Exception: ${e.stackTraceToString()}")
        } finally {
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun addTimer(showAfterAdd: Boolean) {
        if (binding.scheduleLayout.childCount == 1) {
            TimePickerBinding.bind(binding.scheduleLayout[0]).btnDeleteTimer.isEnabled = true
        }

        TimePickerBinding.inflate(layoutInflater, binding.scheduleLayout, false).apply {
            hour.text = Date().apply {
                zero()
            }.formatTime()

            btnSelectTime.setOnClickListener {
                onSelectTime(this)
            }

            btnDeleteTimer.setOnClickListener {
                binding.scheduleLayout.removeView(root)

                if (binding.scheduleLayout.childCount == 1) {
                    TimePickerBinding.bind(binding.scheduleLayout[0])
                        .btnDeleteTimer.isEnabled = false
                }
            }

            if (showAfterAdd)
                btnSelectTime.performClick()
        }
    }

    private fun onSelectTime(timePickerBinding: TimePickerBinding) {
        TimePickerDialog(
            requireContext(), { _, hourOfDay, minute ->
                val pickedTime = Calendar.getInstance().apply {
                    time.zero()
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }.time

                if (binding.scheduleLayout.children
                        .map { TimePickerBinding.bind(it) }
                        .any { it.hour.text == timePickerBinding.hour.text }
                ) {
                    Toast.makeText(
                        requireContext(),
                        R.string.hora_ya_seleccionada,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    timePickerBinding.hour.text = pickedTime.formatTime()

                    // TODO: Comprobar si esto filtra ben
                    for (i in 0 until binding.scheduleLayout.childCount) {
                        if (TimePickerBinding.bind(binding.scheduleLayout[i]).hour.text.toString() > pickedTime.formatTime()) {
                            binding.scheduleLayout.addView(timePickerBinding.root, i)
                            return@TimePickerDialog
                        }
                    }

                    binding.scheduleLayout.addView(timePickerBinding.root)
                }
            },
            0,
            0,
            DateFormat.is24HourFormat(requireContext())
        ).show()
    }

    private fun showHelp() {
        if (binding.textHelp.apply {
                visibility = visibility.xor(View.VISIBLE)
            }.visibility == View.VISIBLE) {

            binding.textHelp.handler.removeCallbacksAndMessages(null)

            binding.textHelp.handler.postDelayed(
                {
                    binding.textHelp.visibility = View.GONE
                }, null, 5000
            )
        } else {
            binding.textHelp.handler.removeCallbacksAndMessages(null)
        }

        Log.d(
            "AddActiveMedDialogFragment.showHelp",
            "Help text visibility: ${binding.textHelp.visibility}"
        )
    }
}