package com.a23pablooc.proxectofct.ui.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatDate
import com.a23pablooc.proxectofct.core.DateTimeUtils.zero
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroDate
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.data.network.CimaApiDefinitions
import com.a23pablooc.proxectofct.databinding.FragmentAddActiveMedBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.ui.view.adapters.TimePickerRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.viewmodel.AddActiveMedViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

@AndroidEntryPoint
class AddActiveMedFragment : Fragment() {

    private lateinit var binding: FragmentAddActiveMedBinding
    private val viewModel: AddActiveMedViewModel by viewModels()

    private var scheduleList: List<Date> = listOf(Calendar.getInstance().time.zero())
    private lateinit var timePickerAdapter: TimePickerRecyclerViewAdapter

    private var fetchedMed: MedicamentoItem? = null
    private var image: Uri = Uri.EMPTY

    private val pickMedia = registerForActivityResult(PickVisualMedia()) {
        it?.let {
            Glide.with(requireContext()).load(it).into(binding.addMedDialogBase.img)
            image = it
        }
    }

    private object BundleKeys {
        const val IMAGE = "image"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddActiveMedBinding.inflate(layoutInflater, container, false)

        timePickerAdapter = TimePickerRecyclerViewAdapter(
            scheduleList,
            onSelectTime = { onSelectTime(it) },
            onRemoveTimer = { onRemoveTimer(it) }
        )

        binding.rvSchedule.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timePickerAdapter
        }

        binding.addMedDialogBase.imgLayout.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(
                    PickVisualMedia.ImageOnly
                )
            )
        }

        binding.addMedDialogBase.imgLayout.setOnLongClickListener {
            Toast.makeText(
                context,
                getString(R.string.seleccionar_una_imagen),
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        binding.addMedDialogBase.btnHelp.setOnClickListener { toggleHelp() }

        binding.addMedDialogBase.btnSearch.setOnClickListener { search() }

        binding.btnStartDatePicker.setOnClickListener {
            onSelectDate(binding.dateStart)
        }

        binding.btnEndDatePicker.setOnClickListener {
            onSelectDate(binding.dateEnd)
        }

        binding.btnAddTimer.setOnClickListener { onAddTimer() }

        binding.addMedDialogBase.favFrame.setOnClickListener {
            binding.addMedDialogBase.ivFavBg.apply {
                visibility = visibility.xor(View.GONE)
            }
        }

        Calendar.getInstance().time.zeroTime().formatDate().let {
            binding.dateStart.text = it
            binding.dateEnd.text = it
        }

        binding.fabAddActiveMed.setOnClickListener { onAddActiveMed() }

        timePickerAdapter.updateData(scheduleList)

        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.let {
            val image = it.getString(BundleKeys.IMAGE)?.toUri()
            this.image = image ?: Uri.EMPTY

            Handler(Looper.getMainLooper()).postDelayed({
                Glide.with(requireContext()).load(
                    image ?: R.mipmap.no_image_available
                ).into(binding.addMedDialogBase.img)
            }, 1)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BundleKeys.IMAGE, image.toString())
    }

    override fun onStop() {
        super.onStop()
        Glide.with(requireContext()).load(R.mipmap.no_image_available)
            .into(binding.addMedDialogBase.img)
    }

    private fun search() {
        val searchingToast: Toast = Toast.makeText(
            context,
            R.string.buscando,
            Toast.LENGTH_SHORT
        ).also { it.show() }

        binding.progressBar.visibility = View.VISIBLE

        val codNacional = binding.addMedDialogBase.codNacional.text.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val fetchedMed = viewModel.search(codNacional)

                this@AddActiveMedFragment.fetchedMed = fetchedMed

                withContext(Dispatchers.Main) {
                    //TODO: Hardcode string
                    if (fetchedMed == null) {
                        Toast.makeText(
                            context,
                            "Medicamento no encontrado",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@withContext
                    }

                    binding.addMedDialogBase.codNacional.setText(codNacional)

                    binding.addMedDialogBase.nombre.setText(fetchedMed.nombre)

                    if (fetchedMed.imagen.toString().startsWith(CimaApiDefinitions.BASE_URL)) {
                        withContext(Dispatchers.IO) {
                            val img = viewModel.downloadImage(
                                fetchedMed.numRegistro,
                                fetchedMed.imagen.toString().substringAfterLast('/')
                            )
                            withContext(Dispatchers.Main) {
                                Glide.with(requireContext()).load(img)
                                    .into(binding.addMedDialogBase.img)
                                image = fetchedMed.imagen
                            }
                        }
                    } else if (fetchedMed.imagen.toString().startsWith("file:///")) {
                        Glide.with(requireContext()).load(fetchedMed.imagen)
                            .into(binding.addMedDialogBase.img)
                        image = fetchedMed.imagen
                    } else {
                        binding.addMedDialogBase.img.setImageResource(R.mipmap.no_image_available)
                        image = Uri.EMPTY
                    }

                    if (fetchedMed.esFavorito) {
                        binding.addMedDialogBase.ivFavBg.visibility = View.VISIBLE
                        binding.addMedDialogBase.favFrame.setOnClickListener {
                            // TODO: Hardcode string
                            Toast.makeText(
                                context,
                                "Este medicamento ya está en favoritos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        binding.addMedDialogBase.ivFavBg.visibility = View.GONE
                        binding.addMedDialogBase.favFrame.setOnClickListener {
                            binding.addMedDialogBase.ivFavBg.apply {
                                visibility = visibility.xor(View.GONE)
                            }
                        }
                    }
                }
            } catch (e: IllegalArgumentException) {
                this@AddActiveMedFragment.fetchedMed = null

                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    Toast.makeText(
                        context,
                        R.string.codigo_nacional_no_valido, // TODO: a.k.a formato incorrecto by regex
                        Toast.LENGTH_SHORT
                    ).show()

                    toggleHelp()
                }
            } catch (e: Exception) {
                this@AddActiveMedFragment.fetchedMed = null

                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun onAddActiveMed() {
        if (!validateForm())
            return

        val nombre = binding.addMedDialogBase.nombre.text.toString()
        val dateStart = DateTimeUtils.parseDate(binding.dateStart.text.toString())
        val dateEnd = DateTimeUtils.parseDate(binding.dateEnd.text.toString())
        val dosis = binding.addMedDialogBase.dosis.text.toString()
        val schedule = scheduleList

        val med = MedicamentoActivoItem(
            pkMedicamentoActivo = 0,
            fkUsuario = viewModel.getUserId(),
            dosis = dosis.ifBlank { "" },
            horario = schedule.toMutableSet(),
            fechaFin = dateEnd,
            fechaInicio = dateStart,
            tomas = mutableMapOf(),
            fkMedicamento = fetchedMed?.apply {
                this.esFavorito = binding.addMedDialogBase.ivFavBg.visibility == View.VISIBLE
                this.imagen = image
                this.nombre = nombre.ifBlank { this.nombre }
                this.fkUsuario = this.fkUsuario.takeIf { it > 0 } ?: viewModel.getUserId()
            } ?: MedicamentoItem(
                pkCodNacionalMedicamento = 0,
                fkUsuario = viewModel.getUserId(),
                nombre = nombre.ifBlank {
                    Toast.makeText(
                        context,
                        "Se debe introducir un nombre", // TODO: Hardcode string
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                },
                imagen = image,
                esFavorito = binding.addMedDialogBase.ivFavBg.visibility == View.VISIBLE,
                numRegistro = "",
                url = "",
                prescripcion = "",
                laboratorio = "",
                prospecto = "",
                afectaConduccion = false,
            )
        )

        viewModel.onDataEntered(med)
    }

    private fun validateForm(): Boolean {
        if (binding.addMedDialogBase.nombre.text.isNullOrBlank()) {
            binding.addMedDialogBase.nombre.error = getString(R.string.sin_nombre)
            Toast.makeText(
                context,
                R.string.sin_nombre,
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (scheduleList.isEmpty()) {
            // TODO: Hardcode string
            Toast.makeText(
                context,
                "Se debe tener al menos una hora de toma",
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
        val today = Calendar.getInstance().time.zeroTime().time

        if (endDate < startDate || endDate < today) {
            Toast.makeText(
                context,
                R.string.fecha_invalida,
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    private fun onAddTimer() {
        onSelectTime(Calendar.getInstance().time.zeroDate())
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
                    time = time.zero()
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }.time

                if (scheduleList.any { it.time == pickedTime.time }) {
                    // TODO: Hardcode string
                    Toast.makeText(
                        context,
                        "Atención: La hora de toma ya está añadida",
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
        val calendar = Calendar.getInstance()

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
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun toggleHelp() {
        if (binding.addMedDialogBase.textHelp.apply {
                visibility = visibility.xor(View.GONE)
            }.visibility == View.VISIBLE) {

            binding.addMedDialogBase.textHelp.handler.removeCallbacksAndMessages(null)

            binding.addMedDialogBase.textHelp.handler.postDelayed({
                binding.addMedDialogBase.textHelp.visibility = View.GONE
            }, null, 5000)
        } else {
            binding.addMedDialogBase.textHelp.handler.removeCallbacksAndMessages(null)
        }
    }
}