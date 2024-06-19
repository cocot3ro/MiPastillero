package com.a23pablooc.proxectofct.ui.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Configuration
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
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
import androidx.appcompat.app.AlertDialog
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
import com.a23pablooc.proxectofct.core.InternalStorageDefinitions
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

    private lateinit var navController: NavController

    private var scheduleList: List<Date> = listOf(
        Calendar.getInstance().apply {
            set(0, 0, 0, 8, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    )

    private var saving = false

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

        binding.img.setOnClickListener { selectImg() }

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

        binding.btnStartDatePicker.setOnClickListener { onSelectDate(binding.dateStart) }

        binding.btnEndDatePicker.setOnClickListener { onSelectDate(binding.dateEnd) }

        binding.btnAddTimer.setOnClickListener { onAddTimer() }

        DateTimeUtils.now.let { now ->
            binding.dateStart.text = now.formatDate()
            Calendar.getInstance().apply {
                time = now
                add(Calendar.DAY_OF_MONTH, 7)
            }.time.let { plusSeven ->
                binding.dateEnd.text = plusSeven.formatDate()
            }
        }

        binding.fabAddActiveMed?.setOnClickListener { onAdd() }

        timePickerAdapter.updateData(scheduleList)

        return binding.root
    }

    private fun onAdd() {
        if (saving) return
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            if (validateForm()) {

                val dialog = AlertDialog.Builder(requireContext())
                    .setCancelable(false)
                    .setView(R.layout.saving_dialog)
                    .show()

                save()

                dialog.dismiss()

                navController.popBackStack()
            }
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

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    menuInflater.inflate(R.menu.menu_toolbar_add_active_med_land, menu)
                } else {
                    menuInflater.inflate(R.menu.menu_toolbar_add_active_med, menu)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.addActiveMed -> {
                        onAdd()
                        true
                    }

                    R.id.select_image -> {
                        selectImg()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun selectImg() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private suspend fun save() {
        saving = true

        val dosis = binding.dosis.text.toString()
        val dateStart = Calendar.getInstance().apply {
            time = DateTimeUtils.parseDate(binding.dateStart.text.toString())
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val dateEnd = Calendar.getInstance().apply {
            time = DateTimeUtils.parseDate(binding.dateEnd.text.toString())
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

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

        val toast = Toast.makeText(
            context,
            getString(R.string.saving),
            Toast.LENGTH_LONG
        )

        toast.show()

        withContext(Dispatchers.IO) {
            viewModel.onDataEntered(med)
        }

        toast.cancel()
    }

    private fun validateForm(): Boolean {
        if (binding.nombre.text.isNullOrBlank()) {
            binding.nombre.error = getString(R.string.should_input_name)
            return false
        }

        if (scheduleList.isEmpty()) {
            Toast.makeText(
                context,
                getString(R.string.should_input_schedule),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (scheduleList.toSet().size != scheduleList.size) {
            Toast.makeText(
                context,
                getString(R.string.unique_schedule_dates),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        val startDate = Calendar.getInstance().apply {
            time = DateTimeUtils.parseDate(binding.dateStart.text.toString())
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val endDate = Calendar.getInstance().apply {
            time = DateTimeUtils.parseDate(binding.dateEnd.text.toString())
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        if (endDate < startDate) {
            Toast.makeText(
                context,
                getString(R.string.invalid_date_end_minor_start),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (endDate < DateTimeUtils.now) {
            Toast.makeText(
                context,
                getString(R.string.invalid_date_end_minor_now),
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
            Toast.makeText(
                context,
                getString(R.string.should_input_schedule),
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
                    Toast.makeText(
                        context,
                        getString(R.string.schedule_already_exists),
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
            getString(R.string.searching),
            Toast.LENGTH_SHORT
        ).also { it.show() }

        binding.progressBar.visibility = View.VISIBLE

        val codNacional = binding.codNacional.text.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val fetchedMed = viewModel.search(codNacional)

                this@AddActiveMedFragment.fetchedMed = fetchedMed

                withContext(Dispatchers.Main) {
                    if (fetchedMed == null) {
                        Toast.makeText(
                            context,
                            getString(R.string.med_not_found),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@withContext
                    }

                    binding.codNacional.setText(codNacional)

                    binding.nombre.setText(fetchedMed.nombre)

                    if (fetchedMed.imagen.toString().startsWith(CimaApiDefinitions.BASE_URL)
                        || fetchedMed.imagen.toString()
                            .startsWith(InternalStorageDefinitions.FILE_PREFIX)
                    ) {
                        Glide.with(requireContext())
                            .load(fetchedMed.imagen)
                            .into(binding.img)

                        image = fetchedMed.imagen
                    } else {
                        binding.img.setImageResource(R.drawable.hide_image_80dp)
                        image = Uri.EMPTY
                    }

                    if (fetchedMed.esFavorito) {
                        binding.ivFavBg.visibility = View.VISIBLE
                        binding.favFrame.setOnClickListener {
                            Toast.makeText(
                                context,
                                getString(R.string.med_already_on_fav),
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
                this@AddActiveMedFragment.fetchedMed = null

                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    Toast.makeText(
                        context,
                        getString(R.string.invalid_national_drug_code),
                        Toast.LENGTH_SHORT
                    ).show()

                    toggleHelp()
                }
            } catch (e: IOException) {
                this@AddActiveMedFragment.fetchedMed = null

                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    Toast.makeText(
                        context,
                        getString(R.string.connection_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                this@AddActiveMedFragment.fetchedMed = null

                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    Toast.makeText(
                        context,
                        getString(R.string.unknown_error),
                        Toast.LENGTH_SHORT
                    ).show()
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
            url = Uri.EMPTY,
            receta = false,
            laboratorio = "",
            prospecto = Uri.EMPTY,
            afectaConduccion = false,
            prescripcion = "",
            principiosActivos = listOf()
        )
    }
}