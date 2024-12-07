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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.core.DateTimeUtils
import com.cocot3ro.mipastillero.core.DateTimeUtils.formatDate
import com.cocot3ro.mipastillero.core.DateTimeUtils.get
import com.cocot3ro.mipastillero.databinding.FragmentEditActiveMedBinding
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.ui.view.adapters.TimePickerRecyclerViewAdapter
import com.cocot3ro.mipastillero.ui.viewmodel.EditActiveMedViewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class EditActiveMedFragment : Fragment() {

    @Inject
    lateinit var gson: Gson

    private lateinit var binding: FragmentEditActiveMedBinding
    private val viewModel: EditActiveMedViewModel by viewModels()

    private lateinit var adapter: TimePickerRecyclerViewAdapter

    private lateinit var navController: NavController

    private lateinit var med: MedicamentoActivoItem

    private var originalMed: MedicamentoActivoItem? = null

    object BundleKeys {
        const val MED_KEY = "med_key"
        const val ORIGINAL_MED_KEY = "original_med_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()

        arguments?.let {
            med = gson.fromJson(it.getString(BundleKeys.MED_KEY), MedicamentoActivoItem::class.java)
            originalMed = originalMed ?: med.copy()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BundleKeys.MED_KEY, gson.toJson(med, MedicamentoActivoItem::class.java))
        outState.putString(
            BundleKeys.ORIGINAL_MED_KEY,
            gson.toJson(originalMed, MedicamentoActivoItem::class.java)
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            med = gson.fromJson(it.getString(BundleKeys.MED_KEY), MedicamentoActivoItem::class.java)
            originalMed = gson.fromJson(
                it.getString(BundleKeys.ORIGINAL_MED_KEY),
                MedicamentoActivoItem::class.java
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditActiveMedBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(navController)

        adapter = TimePickerRecyclerViewAdapter(
            med.horario.toList(),
            onSelectTime = { onSelectTime(it) },
            onRemoveTimer = { onRemoveTimer(it) }
        )

        binding.rvSchedule.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@EditActiveMedFragment.adapter
        }

        if (med.fkMedicamento.imagen.toString().isNotBlank())
            Glide.with(requireContext()).load(med.fkMedicamento.imagen).into(binding.img)

        binding.nombre.text = med.fkMedicamento.nombre

        binding.fabSaveChanges?.setOnClickListener { saveChanges() }

        binding.fabDelete?.setOnClickListener { delete() }

        binding.ibInfo.setOnClickListener {
            navController.popBackStack()
            navController.navigate(R.id.medInfoFragment, Bundle().apply {
                putString(MedInfoFragment.BundleKeys.MED_KEY, gson.toJson(med.fkMedicamento))
            })
        }

        binding.dosis.apply {
            setText(med.dosis)
            addTextChangedListener(
                afterTextChanged = { med.dosis = it.toString() }
            )
            updateFab()
        }

        binding.btnStartDatePicker.setOnClickListener {
            onSelectDate(binding.dateStart) { med.fechaInicio = it }
        }

        binding.btnEndDatePicker.setOnClickListener {
            onSelectDate(binding.dateEnd) { med.fechaFin = it }
        }

        binding.btnAddTimer.setOnClickListener { onAddTimer() }

        binding.dateStart.text = med.fechaInicio.formatDate()
        binding.dateEnd.text = med.fechaFin.formatDate()

        adapter.updateData(med.horario.toList())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    menuInflater.inflate(R.menu.menu_toolbar_edit_active_med_land, menu)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.saveChanged -> {
                        saveChanges()
                        true
                    }

                    R.id.delete -> {
                        delete()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveChanges() {
        if (!validateForm()) return

        viewModel.saveChanges(originalMed!!, med)
        originalMed = med.copy()

        Toast.makeText(
            context,
            getString(R.string.changes_saved),
            Toast.LENGTH_SHORT
        ).show()

        updateFab()
    }

    private fun onSelectDate(textView: TextView, dateSetter: (Date) -> Unit) {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val date = Calendar.getInstance().apply {
                    set(year, monthOfYear, dayOfMonth, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time

                textView.text = date.formatDate()
                dateSetter(date)

                updateFab()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun onAddTimer() {
        onSelectTime(DateTimeUtils.now)
    }

    private fun onSelectTime(date: Date) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val pickedTime = Calendar.getInstance().apply {
                    set(0, 0, 0, hourOfDay, minute, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time

                if (med.horario.any { it.time == pickedTime.time }) {
                    Toast.makeText(
                        context,
                        getString(R.string.schedule_already_exists),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    med.horario =
                        med.horario.apply {
                            remove(date)
                            add(pickedTime)
                        }.sortedBy { it.time }.toMutableSet()
                    adapter.updateData(med.horario.toList())
                }

                updateFab()
            },
            date.get(Calendar.HOUR_OF_DAY),
            date.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private fun onRemoveTimer(date: Date) {
        if (med.horario.size == 1) {
            Toast.makeText(
                context,
                getString(R.string.should_input_schedule),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        med.horario = med.horario.minus(date).toMutableSet()
        adapter.updateData(med.horario.toList())

        updateFab()
    }

    private fun updateFab() {
        binding.fabSaveChanges?.let {
            if (med != originalMed) it.show()
            else it.hide()
        }
    }

    private fun validateForm(): Boolean {
        if (med.horario.isEmpty()) {
            Toast.makeText(
                context,
                getString(R.string.should_input_schedule),
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

        return true
    }

    private fun delete() {
        lifecycleScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                viewModel.deleteMed(med)
                navController.popBackStack()
            }
        }
    }
}