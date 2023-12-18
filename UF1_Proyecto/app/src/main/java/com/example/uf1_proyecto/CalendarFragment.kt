package com.example.uf1_proyecto

import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.uf1_proyecto.databinding.CalendarMedGroupLayoutBinding
import com.example.uf1_proyecto.databinding.CalendarMedLayoutBinding
import com.example.uf1_proyecto.databinding.EmptyLayoutBinding
import com.example.uf1_proyecto.databinding.FragmentCalendarBinding
import java.util.Calendar

class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var pillboxViewModel: PillboxViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        pillboxViewModel = PillboxViewModel.getInstance(requireContext())

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        setHasOptionsMenu(true)

        val navHostFragment =
            (activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.calendarFragment,
                R.id.activeMedFragment,
                R.id.favoriteFragment,
                R.id.diaryFragment
            ),
            binding.drawerLayout
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.toolbar.setOnMenuItemClickListener { menuItem -> menuItemSelected(menuItem) }

        binding.navView.setupWithNavController(navController)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            menuItemSelected(menuItem)
        }

        binding.prevDay.setOnClickListener {
            pillboxViewModel.calendarPrevDay()
            updateView()
        }

        binding.nextDay.setOnClickListener {
            pillboxViewModel.calendarNextDay()
            updateView()
        }

        updateView()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_calendar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun updateView() {
        @Suppress("SetTextI18n")
        binding.calendarDay.text = "${
            DateTimeUtils.millisToDayOfWeek(
                pillboxViewModel.getCalendarCurrDate(),
                requireContext()
            )
        } - ${DateTimeUtils.millisToDate(pillboxViewModel.getCalendarCurrDate())}"

        binding.calendarLayout.removeAllViews()

        val map = pillboxViewModel.getActivosCalendario(pillboxViewModel.getCalendarCurrDate())

        if (map.isEmpty()) {
            EmptyLayoutBinding.inflate(layoutInflater, binding.calendarLayout, true).apply {
                texto.text = getString(R.string.sin_meds_en_dia)
            }
        } else {
            for (entry in map) {
                val groupBinding =
                    CalendarMedGroupLayoutBinding.inflate(
                        layoutInflater,
                        binding.calendarLayout,
                        true
                    )

                val colores =
                    pillboxViewModel.getEstacionColor(pillboxViewModel.getCalendarCurrDate())

                groupBinding.cuerpo.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        colores.second
                    )
                )

                groupBinding.hora.text = DateTimeUtils.millisToTime(entry.key)
                groupBinding.hora.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        colores.first
                    )
                )

                for (medicamento in entry.value) {
                    val calendarEntryBinding = CalendarMedLayoutBinding.inflate(
                        layoutInflater, groupBinding.calendarMedsLayout, true
                    )

                    calendarEntryBinding.nombre.text = medicamento.nombre

                    if (medicamento.imagen != null && medicamento.imagen.isNotEmpty()) {
                        val bitmap = BitmapFactory.decodeByteArray(
                            medicamento.imagen,
                            0,
                            medicamento.imagen.size
                        )
                        calendarEntryBinding.img.setImageBitmap(bitmap)
                    } else {
                        calendarEntryBinding.img.setImageResource(R.mipmap.no_image_available)
                    }

                    if (medicamento.seHaTomado!!) {
                        calendarEntryBinding.btn.setImageResource(android.R.drawable.checkbox_on_background)
                    }

                    calendarEntryBinding.btn.setOnClickListener {
                        if (medicamento.seHaTomado!!) {
                            if (pillboxViewModel.desmarcarToma(
                                    medicamento,
                                    entry.key,
                                    pillboxViewModel.getCalendarCurrDate()
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.desmarcar_ok),
                                    Toast.LENGTH_LONG
                                ).show()
                                medicamento.seHaTomado = false
                                calendarEntryBinding.btn.setImageResource(android.R.drawable.checkbox_off_background)
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.desmarcar_error), Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            if (pillboxViewModel.marcarToma(
                                    medicamento,
                                    entry.key,
                                    pillboxViewModel.getCalendarCurrDate()
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.marcar_ok), Toast.LENGTH_LONG
                                ).show()
                                medicamento.seHaTomado = true
                                calendarEntryBinding.btn.setImageResource(android.R.drawable.checkbox_on_background)
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.marcar_error), Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun search() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                pillboxViewModel.setCalendarCurrDate(
                    DateTimeUtils.createDate(
                        year, monthOfYear, dayOfMonth
                    )
                )

                updateView()
            },
            // Establece la fecha actual como predeterminada
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun today() {
        pillboxViewModel.setCalendarCurrDate(DateTimeUtils.getTodayAsMillis())
        updateView()
    }

    private fun addMedDialog() {
        AddActiveMedDialog(
            requireContext(),
            object : AddActiveMedDialog.OnDataEnteredListener {
                override fun onDataEntered(medicamento: Medicamento) {
                    if (!medicamento.isFavorite!!) {
                        if (pillboxViewModel.addActiveMed(medicamento)) {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_activo_ok),
                                Toast.LENGTH_LONG
                            ).show()
                            updateView()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_activo_error),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        // Si se elige guardar tambien como favorito, se añade a la lista de favoritos
                        val addActive = pillboxViewModel.addActiveMed(medicamento)
                        val addFav = pillboxViewModel.addFavMed(medicamento)

                        if (addActive && addFav) {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_activo_ok),
                                Toast.LENGTH_LONG
                            ).show()
                            updateView()
                        } else if (addActive) {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_fav_error),
                                Toast.LENGTH_LONG
                            ).show()
                            updateView()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_activo_error),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }).show()
    }

    private fun menuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.search -> {
                search()
                true
            }

            R.id.today -> {
                today()
                true
            }

            R.id.addActiveMed -> {
                addMedDialog()
                true
            }

            R.id.historyFragment -> {
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_calendarFragment_to_historyFragment)
                true
            }

            else -> false
        }
    }
}
