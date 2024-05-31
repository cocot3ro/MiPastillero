package com.a23pablooc.proxectofct.ui.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.view.drawToBitmap
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.zero
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.databinding.FragmentAddActiveMedDialogBinding
import com.a23pablooc.proxectofct.databinding.TimePickerBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.ui.view.adapters.TimePickerRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.viewholders.AddActiveMedDialogViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private var image: ByteArray = byteArrayOf()

    private val pickMedia = registerForActivityResult(PickVisualMedia()) {
        it?.let {
            Glide.with(requireContext()).load(it).into(binding.img)

            image = ByteArrayOutputStream().also { byteArrayOutputStream ->
                binding.img.drawToBitmap().compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    byteArrayOutputStream
                )
            }.toByteArray()
        }
    }

    interface OnDataEnteredListener {
        fun onDataEntered(med: MedicamentoActivoItem)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = parentFragment as OnDataEnteredListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement OnDataEnteredListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setView(createView())
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

    private fun createView(): View {
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
                context,
                getString(R.string.seleccionar_una_imagen),
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        binding.btnHelp.setOnClickListener { toggleHelp() }

        binding.btnSearch.setOnClickListener { search() }

        binding.btnAddTimer.setOnClickListener { onAddTimer() }

        binding.favFrame.setOnClickListener {
            binding.btnFavBg.visibility = binding.btnFavBg.visibility.xor(View.GONE)
        }

        onAddTimer()

        return binding.root
    }

    private fun setUpPositiveButton(dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (!validateForm())
                return@setOnClickListener

            val alias = binding.nombre.text.toString()
            val dateStart = DateTimeUtils.parseDate(binding.dateStart.text.toString())
            val dateEnd = DateTimeUtils.parseDate(binding.dateEnd.text.toString())
            val dosis = binding.dosis.text.toString()
            val schedule = scheduleList.toSet()

            dialog.dismiss()

            val med = MedicamentoActivoItem(
                pkMedicamentoActivo = 0,
                dosis = dosis.ifBlank { "" },
                horario = schedule,
                fechaFin = dateEnd,
                fechaInicio = dateStart,
                fkMedicamento = fetchedMed?.apply {
                    esFavorito = binding.btnFavBg.visibility == View.VISIBLE
                } ?: MedicamentoItem(
                    pkCodNacionalMedicamento = 0,
                    nombre = alias,
                    alias = alias,
                    imagen = image,
                    esFavorito = binding.btnFavBg.visibility == View.VISIBLE,
                    numRegistro = "",
                    url = "",
                    prescripcion = "",
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
            Toast.makeText(
                context,
                R.string.sin_nombre,
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        val startDate = DateTimeUtils.parseDate(binding.dateStart.text.toString()).time
        val endDate = DateTimeUtils.parseDate(binding.dateEnd.text.toString()).time
        val today = Date().zeroTime().time

        if (startDate <= endDate || startDate < today) {
            Toast.makeText(
                context,
                R.string.fecha_invalida,
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    private fun search() {
        try {
            Toast.makeText(
                context,
                R.string.buscando,
                Toast.LENGTH_SHORT
            ).show()

            binding.progressBar.visibility = View.VISIBLE

            val codNacional = binding.codNacional.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {
                val fetchedMed = viewModel.search(codNacional)

                withContext(Dispatchers.Main) {
                    //TODO: medicamento_no_encontrado en vez de codigo_nacional_no_encontrado
                    if (fetchedMed == null) {
                        Toast.makeText(
                            context,
                            R.string.codigo_nacional_no_encontrado,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@withContext
                    }

                    this@AddActiveMedDialogFragment.fetchedMed = fetchedMed

                    binding.codNacional.setText(codNacional)

                    binding.nombre.setText(fetchedMed.nombre)

                    if (fetchedMed.imagen.isNotEmpty()) {
                        Glide.with(requireContext()).load(fetchedMed.imagen).into(binding.img)
                        image = fetchedMed.imagen
                    } else {
                        binding.img.setImageResource(R.mipmap.no_image_available)
                        image = byteArrayOf()
                    }

                    if (fetchedMed.esFavorito) {
                        binding.btnFavBg.visibility = View.VISIBLE
                        binding.favFrame.setOnClickListener {}
                    } else {
                        binding.btnFavBg.visibility = View.GONE
                        binding.favFrame.setOnClickListener {
                            binding.btnFavBg.visibility =
                                binding.btnFavBg.visibility.xor(View.GONE)
                        }
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            Toast.makeText(
                context,
                R.string.codigo_nacional_no_valido,
                Toast.LENGTH_SHORT
            ).show()

            toggleHelp()

            Log.e(
                "AddActiveMedDialogFragment.search",
                "IllegalArgumentException: ${e.stackTraceToString()}"
            )
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Log.e("AddActiveMedDialogFragment.search", "Exception: ${e.stackTraceToString()}")
        } finally {
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun onAddTimer() {
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
        scheduleList = scheduleList.minus(date).sortedBy { it.time }
        timePickerAdapter.updateData(scheduleList)

        if (binding.scheduleLayout.childCount == 1) {
            TimePickerBinding.bind(binding.scheduleLayout[0]).btnDeleteTimer.isEnabled = false
        }
    }

    private fun onSelectTime(date: Date) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val pickedTime = Calendar.getInstance().apply {
                    time.zero()
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }.time

                if (scheduleList.any { it.time == pickedTime.time }) {
                    Toast.makeText(
                        context,
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
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private fun toggleHelp() {
        if (binding.textHelp.apply {
                visibility = visibility.xor(View.GONE)
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
    }
}