package com.a23pablooc.proxectofct.ui.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.zero
import com.a23pablooc.proxectofct.databinding.FragmentAddActiveMedDialogBinding
import com.a23pablooc.proxectofct.databinding.TimePickerBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.ui.view.adapters.TimePickerRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.viewholders.AddActiveMedDialogViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.util.Date

@AndroidEntryPoint
class AddActiveMedDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentAddActiveMedDialogBinding
    private val viewModel: AddActiveMedDialogViewModel by viewModels()

    private var scheduleList: List<Date> = emptyList()
    private lateinit var timePickerAdapter: TimePickerRecyclerViewAdapter

    private lateinit var listener: OnDataEnteredListener

    private var fetchedMed: MedicamentoItem? = null
    private lateinit var image: ByteArray

    private val pickMedia = registerForActivityResult(PickVisualMedia()) {
        it?.let {
            Glide.with(requireContext()).load(it).into(binding.img)

            image = ByteArrayOutputStream().also { baos ->
                (binding.img.drawable as BitmapDrawable).bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    baos
                )
            }.toByteArray()
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

        timePickerAdapter = TimePickerRecyclerViewAdapter(
            scheduleList,
            onSelectTime = {
                onSelectTime(it)
            },
            onRemoveTimer = {
                onRemoveTimer(it)
            }
        )

        binding.scheduleLayout.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timePickerAdapter
        }

        binding.imgLayout.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(
                    PickVisualMedia.ImageOnly
                )
            )
        }

        binding.favFrame.setOnLongClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.seleccionar_una_imagen), Toast.LENGTH_SHORT
            ).show()
            true
        }

        binding.btnHelp.setOnClickListener { showHelp() }

        binding.btnSearch.setOnClickListener { search() }

        binding.btnAddTimer.setOnClickListener { onAddTimer(true) }

        binding.favFrame.setOnClickListener {
            binding.btnFavBg.visibility = binding.btnFavBg.visibility.xor(View.VISIBLE)
        }

        onAddTimer(false)

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
            }.create().apply {
                setOnShowListener {
                    setUpPositiveButton(this)
                }
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setUpPositiveButton(dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (!validateForm())
                return@setOnClickListener

            val nombre = binding.nombre.text.toString()
            val dateStart = DateTimeUtils.parseDate(binding.dateStart.text.toString())
            val dateEnd = DateTimeUtils.parseDate(binding.dateEnd.text.toString())
            val dosis = binding.dosis.text.toString()
            val schedule = binding.scheduleLayout.children
                .map { DateTimeUtils.parseTime(TimePickerBinding.bind(it).hour.text.toString()) }
                .toSet()

            dialog.dismiss()

            //TODO: todo
            val med = MedicamentoActivoItem(
                pkMedicamentoActivo = 0,
                dosis = dosis,
                horario = schedule,
                fechaFin = dateEnd,
                fechaInicio = dateStart,
                fkMedicamento = fetchedMed ?: MedicamentoItem(
                    pkMedicamento = 0,
                    numRegistro = "",
                    nombre = nombre,
                    alias = nombre,
                    imagen = image,
                    url = "",
                    prescripcion = "",
                    esFavorito = binding.btnFavBg.visibility == View.VISIBLE,
                    laboratorio = "",
                    prospecto = "",
                    afectaConduccion = false
                )
            )

            listener.onDataEntered(med)
        }
    }

    private fun validateForm(): Boolean {
        if (binding.nombre.text.isNullOrBlank()) {
            Toast.makeText(requireContext(), R.string.sin_nombre, Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.dateStart.text.toString() <= binding.dateEnd.text.toString()) {
            Toast.makeText(requireContext(), R.string.fecha_invalida, Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun search() {
        try {
            Toast.makeText(requireContext(), R.string.buscando, Toast.LENGTH_SHORT).show()

            binding.progressBar.visibility = View.VISIBLE

            val codNacional = binding.codNacional.text.toString()

            val fetchedMed = viewModel.search(codNacional)

            if (fetchedMed == null) {
                Toast.makeText(
                    requireContext(),
                    R.string.codigo_nacional_no_encontrado,
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            this.fetchedMed = fetchedMed

            binding.codNacional.setText(codNacional)

            binding.nombre.setText(fetchedMed.nombre)

            binding.btnFavBg.apply {
                visibility = if (fetchedMed.esFavorito) View.VISIBLE else View.GONE
            }

            binding.favFrame.setOnClickListener {}

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

    private fun onAddTimer(showAfterAdd: Boolean) {
        if (binding.scheduleLayout.childCount == 1) {
            TimePickerBinding.bind(binding.scheduleLayout[0]).btnDeleteTimer.isEnabled = true
        }

        val newTime = Date().apply {
            zero()
        }

        scheduleList = scheduleList.plus(newTime).sortedBy { it.time }

        timePickerAdapter.updateData(scheduleList)
    }

    private fun onRemoveTimer(date: Date) {
        scheduleList = scheduleList.minus(date).sortedBy { it.time}
        timePickerAdapter.updateData(scheduleList)

        if (binding.scheduleLayout.childCount == 1) {
            TimePickerBinding.bind(binding.scheduleLayout[0]).btnDeleteTimer.isEnabled = false
        }
    }

    private fun onSelectTime(date: Date) {
        TimePickerDialog(
            requireContext(), { _, hourOfDay, minute ->
                val pickedTime = Calendar.getInstance().apply {
                    time.zero()
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }.time

                if (scheduleList.any { it.time == pickedTime.time }) {
                    Toast.makeText(
                        requireContext(),
                        R.string.hora_ya_seleccionada,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    date.time = pickedTime.time
                    timePickerAdapter.updateData(scheduleList)
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