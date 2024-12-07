package com.cocot3ro.mipastillero.ui.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Configuration
import android.icu.util.Calendar
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.core.DateTimeUtils
import com.cocot3ro.mipastillero.core.DateTimeUtils.formatDate
import com.cocot3ro.mipastillero.databinding.FragmentReuseMedBinding
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem
import com.cocot3ro.mipastillero.ui.view.adapters.TimePickerRecyclerViewAdapter
import com.cocot3ro.mipastillero.ui.viewmodel.ReuseMedViewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class ReuseMedFragment : Fragment() {
    @Inject
    lateinit var gson: Gson

    private lateinit var binding: FragmentReuseMedBinding
    private val viewModel: ReuseMedViewModel by viewModels()

    private lateinit var navController: NavController

    private lateinit var adapter: TimePickerRecyclerViewAdapter

    private var scheduleList: List<Date> = listOf(
        Calendar.getInstance().apply {
            set(0, 0, 0, 8, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    )

    private var saving = false

    private lateinit var med: MedicamentoItem

    object BundleKeys {
        const val MED_KEY = "med_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            med = gson.fromJson(it.getString(BundleKeys.MED_KEY), MedicamentoItem::class.java)
        }
        navController = findNavController()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BundleKeys.MED_KEY, gson.toJson(med, MedicamentoItem::class.java))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReuseMedBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(findNavController())

        adapter = TimePickerRecyclerViewAdapter(
            scheduleList,
            onSelectTime = { onSelectTime(it) },
            onRemoveTimer = { onRemoveTimer(it) }
        )

        binding.nombre.text = med.nombre

        if (med.imagen.toString().isNotBlank())
            Glide.with(requireContext()).load(med.imagen).into(binding.img)

        binding.rvSchedule.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@ReuseMedFragment.adapter
        }

        binding.btnStartDatePicker.setOnClickListener { onSelectDate(binding.dateStart) }

        binding.btnEndDatePicker.setOnClickListener { onSelectDate(binding.dateEnd) }

        binding.btnAddTimer.setOnClickListener { onAddTimer() }

        DateTimeUtils.now.formatDate().let {
            binding.dateStart.text = it
            binding.dateEnd.text = it
        }

        binding.fabAddActiveMed?.setOnClickListener { onAdd() }

        adapter.updateData(scheduleList)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    menuInflater.inflate(R.menu.menu_toolbar_reuse_med_land, menu)
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

    private fun onAdd() {
        if (saving) return

        if (validateForm()) {
            save()
            navController.popBackStack()
        }
    }

    private fun save() {
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
            fkUsuario = med.fkUsuario,
            dosis = dosis.ifBlank { "" },
            horario = schedule.toMutableSet(),
            fechaFin = dateEnd,
            fechaInicio = dateStart,
            tomas = mutableMapOf(),
            fkMedicamento = med
        )

        viewModel.onDataEntered(med)
    }

    private fun validateForm(): Boolean {
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
        adapter.updateData(scheduleList)
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
                    adapter.updateData(scheduleList)
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
}