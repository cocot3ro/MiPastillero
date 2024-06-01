package com.a23pablooc.proxectofct.ui.view.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.view.drawToBitmap
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatDate
import com.a23pablooc.proxectofct.core.DateTimeUtils.zero
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroDate
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.databinding.FragmentAddActiveMedDialogBinding
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

    private var scheduleList: List<Date> = listOf(Date().zero())
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

    companion object {
        const val TAG = "AddActiveMedDialogFragment"
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
            onSelectTime = { onSelectTime(it) },
            onRemoveTimer = { onRemoveTimer(it) }
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

        binding.imgLayout.setOnLongClickListener {
            Toast.makeText(
                context,
                getString(R.string.seleccionar_una_imagen),
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        binding.btnHelp.setOnClickListener { toggleHelp() }

        binding.btnSearch.setOnClickListener { search() }

        binding.btnStartDatePicker.setOnClickListener {
            onSelectDate(binding.dateStart)
        }

        binding.btnEndDatePicker.setOnClickListener {
            onSelectDate(binding.dateEnd)
        }

        binding.btnAddTimer.setOnClickListener { onAddTimer() }

        binding.favFrame.setOnClickListener {
            binding.btnFavBg.visibility = binding.btnFavBg.visibility.xor(View.GONE)
        }

        Date().zeroTime().formatDate().also {
            binding.dateStart.text = it
            binding.dateEnd.text = it
        }

        timePickerAdapter.updateData(scheduleList)

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
            val schedule = scheduleList

            dialog.dismiss()

            val med = MedicamentoActivoItem(
                pkMedicamentoActivo = 0,
                dosis = dosis.ifBlank { "" },
                horario = schedule.toMutableSet(),
                fechaFin = dateEnd,
                fechaInicio = dateStart,
                tomas = mutableMapOf(),
                fkMedicamento = fetchedMed?.apply {
                    esFavorito = binding.btnFavBg.visibility == View.VISIBLE
                    this.alias = alias.ifBlank { this.alias }
                    this.imagen = image
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

        if (scheduleList.toSet().size != scheduleList.size) {
            // TODO: Hardcode string
            Toast.makeText(
                context,
                "Las horas de toma deben de ser únicas",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        val startDate = DateTimeUtils.parseDate(binding.dateStart.text.toString()).time
        val endDate = DateTimeUtils.parseDate(binding.dateEnd.text.toString()).time
        val today = Date().zeroTime().time

        if (startDate < endDate || startDate < today) {
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
        binding.progressBar.visibility = View.VISIBLE

        val codNacional = binding.codNacional.text.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val fetchedMed = viewModel.search(codNacional).also {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            R.string.buscando,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

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
                        binding.favFrame.setOnClickListener {
                            // TODO: Hardcode string
                            Toast.makeText(
                                context,
                                "Este medicamento ya está en favoritos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        binding.btnFavBg.visibility = View.GONE
                        binding.favFrame.setOnClickListener {
                            binding.btnFavBg.visibility =
                                binding.btnFavBg.visibility.xor(View.GONE)
                        }
                    }
                }
            } catch (e: IllegalArgumentException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        R.string.codigo_nacional_no_valido,
                        Toast.LENGTH_SHORT
                    ).show()

                    toggleHelp()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

    }

    private fun onAddTimer() {
        val newTime = Date().apply {
            zero()
        }

        scheduleList = scheduleList.plus(newTime).sortedBy { it.time }

        timePickerAdapter.updateData(scheduleList)
    }

    private fun onRemoveTimer(date: Date) {
        if (scheduleList.size == 1) {
            // TODO: Hardcode string
            Toast.makeText(
                context,
                "Se debe tener al menos una hora de toma",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        scheduleList = scheduleList.minus(date)
        timePickerAdapter.updateData(scheduleList)
    }

    private fun onSelectTime(date: Date) {
        val calendar = Calendar.getInstance().apply {
            time = date
        }

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val pickedTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time.zeroDate()

                if (scheduleList.any { it.time == pickedTime.time }) {
                    Toast.makeText(
                        context,
                        R.string.hora_ya_seleccionada,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    scheduleList = scheduleList.minus(date).plus(pickedTime).sortedBy { it.time }
                    timePickerAdapter.updateData(scheduleList)
                }

            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private fun onSelectDate(textView: TextView) {
        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val date = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, monthOfYear)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }.time.zeroTime()

                textView.text = date.formatDate()
            },
            // Establece la fecha actual como predeterminada
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
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