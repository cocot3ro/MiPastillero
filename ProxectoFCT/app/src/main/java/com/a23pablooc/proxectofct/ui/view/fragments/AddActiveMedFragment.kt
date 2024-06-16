package com.a23pablooc.proxectofct.ui.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Configuration
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatDate
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.network.CimaApiDefinitions
import com.a23pablooc.proxectofct.databinding.FragmentAddActiveMedBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.ui.view.adapters.TimePickerRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.viewmodel.AddActiveMedViewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class AddActiveMedFragment : Fragment() {

    @Inject
    lateinit var userInfoProvider: UserInfoProvider

    @Inject
    lateinit var gson: Gson

    private lateinit var binding: FragmentAddActiveMedBinding
    private val viewModel: AddActiveMedViewModel by viewModels()

    private var scheduleList: List<Date> = listOf(
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    )

    private lateinit var timePickerAdapter: TimePickerRecyclerViewAdapter

    private var fetchedMed: MedicamentoItem? = null
    private var image: Uri = Uri.EMPTY

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        it?.let {
            Glide.with(requireContext()).load(it).into(binding.img)
            image = it
        }
    }

    private object BundleKeys {
        const val MED = "med"
        const val IS_FAV = "is_fav"
        const val IMAGE = "image"
        const val DATE_START = "date_start"
        const val DATE_END = "date_end"
        const val SCHEDULE = "schedule"
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddActiveMedBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(findNavController())

        binding.imgLayout.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
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

        binding.favFrame.setOnClickListener {
            binding.ivFavBg.apply {
                visibility = visibility.xor(View.GONE)
            }
        }

        timePickerAdapter = TimePickerRecyclerViewAdapter(
            scheduleList,
            onSelectTime = { onSelectTime(it) },
            onRemoveTimer = { onRemoveTimer(it) }
        )

        binding.rvSchedule.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = timePickerAdapter
        }

        binding.btnStartDatePicker.setOnClickListener {
            onSelectDate(binding.dateStart)
        }

        binding.btnEndDatePicker.setOnClickListener {
            onSelectDate(binding.dateEnd)
        }

        binding.btnAddTimer.setOnClickListener { onAddTimer() }

        DateTimeUtils.now.formatDate().let {
            binding.dateStart.text = it
            binding.dateEnd.text = it
        }

        binding.fabAddActiveMed?.setOnClickListener { onAdd() }

        timePickerAdapter.updateData(scheduleList)

        return binding.root
    }

    private fun onAdd() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            if (onSave()) navController.popBackStack()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { bundle ->
            binding.dateStart.text = bundle.getString(BundleKeys.DATE_START)
            binding.dateEnd.text = bundle.getString(BundleKeys.DATE_END)
            scheduleList = bundle.getLongArray(BundleKeys.SCHEDULE)!!.map { Date(it) }

            timePickerAdapter.updateData(scheduleList)

            val image = bundle.getString(BundleKeys.IMAGE)?.toUri()
            this.image = image ?: Uri.EMPTY

            if (this.image.toString().isNotBlank()) {
                Glide.with(requireContext()).load(this.image).into(binding.img)
            }

            this.fetchedMed =
                gson.fromJson(
                    bundle.getString(BundleKeys.MED),
                    MedicamentoItem::class.java
                )

            binding.ivFavBg.visibility =
                if (bundle.getBoolean(BundleKeys.IS_FAV)) View.VISIBLE else View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BundleKeys.DATE_START, binding.dateStart.text.toString())
        outState.putString(BundleKeys.DATE_END, binding.dateEnd.text.toString())
        outState.putLongArray(BundleKeys.SCHEDULE, scheduleList.map { it.time }.toLongArray())
        outState.putString(
            BundleKeys.MED,
            gson.toJson(fetchedMed, MedicamentoItem::class.java)
        )
        outState.putString(BundleKeys.IMAGE, image.toString())
        outState.putBoolean(BundleKeys.IS_FAV, binding.ivFavBg.visibility == View.VISIBLE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    menuInflater.inflate(R.menu.menu_toolbar_add_active_med_land, menu)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.addActiveMed -> {
                        onAdd()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private suspend fun onSave(): Boolean {
        if (!validateForm())
            return false

        val dosis = binding.dosis.text.toString()
        val dateStart = DateTimeUtils.parseDate(binding.dateStart.text.toString())
        val dateEnd = DateTimeUtils.parseDate(binding.dateEnd.text.toString())
        val schedule = scheduleList

        val med = MedicamentoActivoItem(
            pkMedicamentoActivo = 0,
            fkUsuario = userInfoProvider.currentUser.pkUsuario,
            dosis = dosis.ifBlank { "" },
            horario = schedule.toMutableSet(),
            fechaFin = dateEnd,
            fechaInicio = dateStart,
            tomas = mutableMapOf(),
            fkMedicamento = requireMed()
        )

        withContext(Dispatchers.IO) {
            viewModel.onDataEntered(med)
        }

        return true
    }

    private fun validateForm(): Boolean {
        if (binding.nombre.text.isNullOrBlank()) {
            binding.nombre.error = getString(R.string.sin_nombre)
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

        if (endDate < startDate) {
            Toast.makeText(
                context,
                R.string.fecha_invalida,
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (endDate < DateTimeUtils.now.time) {
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
        onSelectTime(DateTimeUtils.now)
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
                    set(0, 0, 0, hourOfDay, minute, 0)
                    set(Calendar.MILLISECOND, 0)
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
                    set(year, monthOfYear, dayOfMonth, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time

                textView.text = date.formatDate()
            },
            // Establece la fecha actual como predeterminada
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun search() {
        val searchingToast: Toast = Toast.makeText(
            context,
            R.string.buscando,
            Toast.LENGTH_SHORT
        ).also { it.show() }

        binding.progressBar.visibility = View.VISIBLE

        val codNacional = binding.codNacional.text.toString()

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

                    binding.codNacional.setText(codNacional)

                    binding.nombre.setText(fetchedMed.nombre)

                    if (fetchedMed.imagen.toString().startsWith(CimaApiDefinitions.BASE_URL)) {
                        withContext(Dispatchers.IO) {
                            val img = viewModel.downloadImage(
                                fetchedMed.numRegistro,
                                fetchedMed.imagen.toString().substringAfterLast('/')
                            )
                            withContext(Dispatchers.Main) {
                                Glide.with(requireContext()).load(img)
                                    .into(binding.img)
                                image = fetchedMed.imagen
                            }
                        }
                    } else if (fetchedMed.imagen.toString().startsWith("file:///")) {
                        Glide.with(requireContext()).load(fetchedMed.imagen)
                            .into(binding.img)
                        image = fetchedMed.imagen
                    } else {
                        binding.img.setImageResource(R.mipmap.no_image_available)
                        image = Uri.EMPTY
                    }

                    if (fetchedMed.esFavorito) {
                        binding.ivFavBg.visibility = View.VISIBLE
                        binding.favFrame.setOnClickListener {
                            // TODO: Hardcode string
                            Toast.makeText(
                                context,
                                "Este medicamento ya está en favoritos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        binding.ivFavBg.visibility = View.GONE
                        binding.favFrame.setOnClickListener {
                            binding.ivFavBg.apply {
                                visibility = visibility.xor(View.GONE)
                            }
                        }
                    }
                }
            } catch (e: IllegalArgumentException) {
                Log.e(
                    "AddActiveMedFragment",
                    "IllegalFormatFlagsException: " + (e.message ?: "Unknown error"),
                    e
                )

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
            } catch (e: IOException) {
                Log.e(
                    "AddActiveMedFragment",
                    "IOException: " + (e.message ?: "Unknown error"),
                    e
                )

                this@AddActiveMedFragment.fetchedMed = null

                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    // TODO: Hardcode string
                    Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("AddActiveMedFragment", "Exception: " + (e.message ?: "Unknown error"), e)

                this@AddActiveMedFragment.fetchedMed = null

                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    // TODO: Hardcode string
                    Toast.makeText(context, "Error desconocido", Toast.LENGTH_SHORT).show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun toggleHelp() {
        binding.textHelp.apply {
            visibility = visibility.xor(View.GONE)
            handler.removeCallbacksAndMessages(null)

            if (visibility == View.VISIBLE) {
                handler.postDelayed({
                    visibility = View.GONE
                }, 5000)
            }
        }
    }

    private fun requireMed(): MedicamentoItem {
        val nombre = binding.nombre.text.toString()

        return fetchedMed?.apply {
            this.esFavorito = binding.ivFavBg.visibility == View.VISIBLE
            this.imagen = image
            this.nombre = nombre.ifBlank { this.nombre }
            this.fkUsuario =
                this.fkUsuario.takeIf { it > 0 } ?: userInfoProvider.currentUser.pkUsuario
        } ?: MedicamentoItem(
            pkCodNacionalMedicamento = 0,
            fkUsuario = userInfoProvider.currentUser.pkUsuario,
            nombre = nombre,
            imagen = image,
            esFavorito = binding.ivFavBg.visibility == View.VISIBLE,
            numRegistro = "",
            url = "",
            prescripcion = "",
            laboratorio = "",
            prospecto = "",
            afectaConduccion = false,
        )
    }
}